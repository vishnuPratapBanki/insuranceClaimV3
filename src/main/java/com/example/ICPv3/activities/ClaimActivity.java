package com.example.ICPv3.activities;

import com.example.ICPv3.exceptions.NonRetryableClaimException;
import io.temporal.activity.ActivityInterface;
import org.springframework.stereotype.Component;

@Component
@ActivityInterface
public interface ClaimActivity {
    void validateClaim(String claimId) throws NonRetryableClaimException;

    void detectFraud(String claimId);

    void processApproval(String claimId);

    void initiatePayment(String claimId);

    void compensateClaim(String claimId);

    void compensateValidation(String claimId);

    void compensateFraudDetection(String claimId);

    void compensateApproval(String claimId);
}
