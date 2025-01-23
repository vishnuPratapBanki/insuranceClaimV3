package com.example.ICPv3.activities;

import com.example.ICPv3.exceptions.NonRetryableClaimException;
import com.example.ICPv3.models.Claim;
import com.example.ICPv3.repositories.ClaimRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class ClaimActivityImpl implements ClaimActivity {
    @Autowired
    private ClaimRepository claimRepository;

    // Failure toggles for testing
    @Setter
    private boolean failValidation = false;
    @Setter
    private boolean failFraudDetection = false;
    @Setter
    private boolean failApproval = false;
    @Setter
    private boolean failPayment = false;

    @Override
    public void validation(String claimId) {
        if (failValidation) throw new RuntimeException("Validation failed for testing purposes.");

        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        claim.setStatus("Validated");
        claimRepository.save(claim);
    }

    @Override
    public void fraudDetection(String claimId) {
        if (failFraudDetection) throw new RuntimeException("Fraud detection failed for testing purposes.");

        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        try {
            Thread.sleep(10000); // Simulate processing for 10 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException("Task interrupted", e);
        }
        claim.setFraudStatus("No Fraud Detected");
        claimRepository.save(claim);
    }

    @Override
    public void initiatePayment(String claimId) {
        if (failPayment) throw new RuntimeException("Payment failed for testing purposes.");

        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        // Simulate failure based on random probability or manual flag
        if ( Math.random() < 0.3) { // 30% chance of failure
            throw new RuntimeException("Payment initiation failed");
        }

        claim.setStatus("Payment Initiated");
        claimRepository.save(claim);
    }

    @Override
    public void approval(String claimId) {
        if (failApproval) throw new RuntimeException("Approval failed for testing purposes.");

        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        claim.setApprovalStatus("Approved");
        claimRepository.save(claim);
    }

    @Override
    public void compensatePayment(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();
        claim.setStatus("Payment Compensation Triggered");
        claimRepository.save(claim);
    }

    @Override
    public void compensateValidation(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        claim.setStatus("Validation Rolled Back");
        claimRepository.save(claim);
    }

    @Override
    public void compensateFraudDetection(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        claim.setFraudStatus("Fraud Check Rolled Back");
        claimRepository.save(claim);
    }

    @Override
    public void compensateApproval(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() ->
                new IllegalArgumentException("Claim not found with ID: " + claimId)
        );
        claim.setApprovalStatus("Approval Rolled Back");
        claimRepository.save(claim);
    }

}
