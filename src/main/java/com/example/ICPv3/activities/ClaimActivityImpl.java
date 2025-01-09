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

    // Setter for manual failure flag
    @Setter

    // Flag for manual failure
    private boolean manualFailure = false;

    @Override
    public void validateClaim(String claimId) throws NonRetryableClaimException {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NonRetryableClaimException("Claim not found"));
        if (!claim.getPolicyHolderName().equalsIgnoreCase("Active")) {
            throw new NonRetryableClaimException("Policy is inactive");
        }
    }

    @Override
    public void detectFraud(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();
        if (claim.getClaimType().equalsIgnoreCase("HighRisk")) {
            throw new RuntimeException("Fraudulent claim detected");
        }
    }

    @Override
    public void processApproval(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();
        claim.setStatus("Approved");
        claimRepository.save(claim);
    }

    @Override
    public void initiatePayment(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();

        // Simulate failure based on random probability or manual flag
        if (manualFailure || Math.random() < 0.3) { // 30% chance of failure
            throw new RuntimeException("Payment initiation failed");
        }

        claim.setStatus("Payment Initiated");
        claimRepository.save(claim);
    }

    @Override
    public void compensateClaim(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();
        claim.setStatus("Compensation Triggered");
        claimRepository.save(claim);
    }

    @Override
    public void compensateValidation(String claimId) {

    }

    @Override
    public void compensateFraudDetection(String claimId) {

    }

    @Override
    public void compensateApproval(String claimId) {

    }

}
