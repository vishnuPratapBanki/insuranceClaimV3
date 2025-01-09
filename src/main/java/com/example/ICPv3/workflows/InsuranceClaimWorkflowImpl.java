package com.example.ICPv3.workflows;

import com.example.ICPv3.activities.ClaimActivity;
import com.example.ICPv3.exceptions.NonRetryableClaimException;
import com.example.ICPv3.models.Claim;
import com.example.ICPv3.repositories.ClaimRepository;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InsuranceClaimWorkflowImpl implements InsuranceClaimWorkflow {

    private final ClaimRepository claimRepository;
    private final ClaimActivity activity;
    private String claimStatus = "Pending";

    // Constructor to inject dependencies explicitly
    public InsuranceClaimWorkflowImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
        this.activity = Workflow.newActivityStub(
                ClaimActivity.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(30))
                        .build());
    }

    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1)) // Wait 1 second before first retry
            .setMaximumInterval(Duration.ofSeconds(20)) // Do not exceed 20 seconds between retries
            .setBackoffCoefficient(2) // Wait 1 second, then 2, then 4, etc
            .setMaximumAttempts(3) // Fail after 5000 attempts
            .build();

    // ActivityOptions specify the limits on how long an Activity can execute before
    // being interrupted by the Orchestration service
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            .setRetryOptions(retryoptions) // Apply the RetryOptions defined above
            .setStartToCloseTimeout(Duration.ofSeconds(10)) // Max execution time for single Activity
            .setScheduleToCloseTimeout(Duration.ofSeconds(5000)) // Entire duration from scheduling to completion including queue time
            .build();

    private final ClaimActivity activity =
            Workflow.newActivityStub(ClaimActivity.class,defaultActivityOptions);


    @Override
    public void processClaim(String claimId) {

        if (claimRepository == null) {
            Workflow.getLogger(this.getClass()).error("ClaimRepository is null");
            throw new RuntimeException("ClaimRepository is not injected");
        }

        Claim claim = claimRepository.findById(claimId).orElseThrow();
        List<Runnable> compensationSteps = new ArrayList<>();

        try {
            activity.validateClaim(claimId);
            claim.setStatus("Validated");
            claimRepository.save(claim);

            // Add compensation step for validation
            compensationSteps.add(() -> activity.compensateValidation(claimId));

            activity.detectFraud(claimId);
            claim.setFraudStatus("Fraud Check Passed");
            claimRepository.save(claim);

            // Add compensation step for fraud detection
            compensationSteps.add(() -> activity.compensateFraudDetection(claimId));

            activity.processApproval(claimId);
            claim.setApprovalStatus("Approved");
            claimRepository.save(claim);

            // Add compensation step for approval
            compensationSteps.add(() -> activity.compensateApproval(claimId));

            activity.initiatePayment(claimId);
            claim.setPaymentStatus("Payment Initiated");
            claimRepository.save(claim);

        } catch (NonRetryableClaimException e) {
            claim.setStatus("Rejected");
            claimRepository.save(claim);
            Workflow.getLogger(this.getClass()).info("Claim rejected: " + e.getMessage());
        } catch (Exception e) {
            claim.setStatus("Failed");
            claimRepository.save(claim);
            Workflow.getLogger(this.getClass()).info("Workflow failed, triggering compensation: " + e.getMessage());

            // Trigger compensation in reverse order
            for (int i = compensationSteps.size() - 1; i >= 0; i--) {
                try {
                    compensationSteps.get(i).run();
                } catch (Exception ex) {
                    Workflow.getLogger(this.getClass()).error("Compensation step failed: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public void updateClaimDetails(String claimId, String newStatus) {
        this.claimStatus = newStatus;
        Workflow.getLogger(this.getClass()).info("Claim status updated via signal: " + newStatus);
    }

    @Override
    public String getCurrentClaimStatus() {
        return claimStatus;
    }
}

