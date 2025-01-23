package com.example.ICPv3.activities;

import com.example.ICPv3.exceptions.NonRetryableClaimException;
import io.temporal.activity.ActivityInterface;
import org.springframework.stereotype.Component;

@Component
@ActivityInterface
public interface ClaimActivity {
    void validation(String claimId) throws NonRetryableClaimException;

    void fraudDetection(String claimId);

    void approval(String claimId);

    void initiatePayment(String claimId);

    void compensatePayment(String claimId);

    void compensateValidation(String claimId);

    void compensateFraudDetection(String claimId);

    void compensateApproval(String claimId);
}
