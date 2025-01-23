package com.example.ICPv3.workflows;

import com.example.ICPv3.activities.ClaimActivity;
import com.example.ICPv3.exceptions.NonRetryableClaimException;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InsuranceClaimWorkflowImpl implements InsuranceClaimWorkflow {

    private final RetryOptions retryOptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(20))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(3)
            .build();

    private final ActivityOptions activityOptions = ActivityOptions.newBuilder()
            .setRetryOptions(retryOptions)
            .setStartToCloseTimeout(Duration.ofSeconds(20))
            .setScheduleToCloseTimeout(Duration.ofMinutes(5))
            .build();

    private final ClaimActivity activity = Workflow.newActivityStub(ClaimActivity.class, activityOptions);

    private String claimStatus = "Pending";
    private final List<Runnable> compensationSteps = new ArrayList<>();

    @Override
    public void processClaim(String claimId) {
        Workflow.getLogger(this.getClass()).info("Workflow started for Claim ID: " + claimId);

        try {
            // Step 1: Validate Claim
            activity.validation(claimId);
            updateStatus("Validated");
            addCompensationStep(() -> activity.compensateValidation(claimId));

            // Step 2: Detect Fraud
            activity.fraudDetection(claimId);
            updateStatus("Fraud Check Passed");
            addCompensationStep(() -> activity.compensateFraudDetection(claimId));

            // Step 3: Process Approval
            activity.approval(claimId);
            updateStatus("Approved");
            addCompensationStep(() -> activity.compensateApproval(claimId));

            // Step 4: Initiate Payment
            activity.initiatePayment(claimId);
            updateStatus("Payment Initiated");
            addCompensationStep(() -> activity.compensatePayment(claimId));

            Workflow.getLogger(this.getClass()).info("Workflow completed successfully for Claim ID: " + claimId);
        } catch (NonRetryableClaimException e) {
            updateStatus("Rejected");
            Workflow.getLogger(this.getClass()).info("Claim rejected: " + e.getMessage());
        } catch (Exception e) {
            updateStatus("Failed");
            Workflow.getLogger(this.getClass()).error("Workflow failed: " + e.getMessage());
            triggerCompensation();
            throw Workflow.wrap(e); // Rethrow as non-retryable
        }
    }

    @Override
    @SignalMethod
    public void updateClaimDetails(String claimId, String newStatus) {
        this.claimStatus = newStatus;
        Workflow.getLogger(this.getClass()).info("Claim status updated via signal: " + newStatus);
    }

    @Override
    public String getCurrentClaimStatus() {
        return claimStatus;
    }

    // Helper method to update workflow status
    private void updateStatus(String status) {
        this.claimStatus = status;
        Workflow.getLogger(this.getClass()).info("Workflow status updated to: " + status);
    }

    // Add compensation step to the list
    private void addCompensationStep(Runnable compensationAction) {
        compensationSteps.add(compensationAction);
        Workflow.getLogger(this.getClass()).info("Compensation step added.");
    }

    // Trigger compensation in reverse order
    private void triggerCompensation() {
        Workflow.getLogger(this.getClass()).info("Triggering compensation steps...");
        for (int i = compensationSteps.size() - 1; i >= 0; i--) {
            try {
                compensationSteps.get(i).run();
            } catch (Exception e) {
                Workflow.getLogger(this.getClass()).error("Compensation step failed: " + e.getMessage());
            }
        }
    }
}

