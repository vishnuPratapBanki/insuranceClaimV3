package com.example.ICPv3;

import com.example.ICPv3.activities.ClaimActivity;
import com.example.ICPv3.activities.ClaimActivityImpl;
import com.example.ICPv3.models.Claim;
import com.example.ICPv3.repositories.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimActivityImplTest {

    private ClaimActivityImpl claimActivity;
    private ClaimRepository claimRepository;

    @BeforeEach
    void setUp() {
        claimRepository = mock(ClaimRepository.class);
        claimActivity = new ClaimActivityImpl();
        claimActivity.setClaimRepository(claimRepository); // Set the mocked repository
    }
//        @Mock
//        private ClaimRepository claimRepository;
//
//        @InjectMocks
//        private ClaimActivity claimActivity;

    // Helper method to create a mock Claim
    private Claim createMockClaim(String id) {
        Claim claim = new Claim();
        claim.setId(id);
        return claim;
    }

    // 1. Validation Tests
    @Test
    void testValidation_Success() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.validation(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Validated", claim.getStatus());
    }

    @Test
    void testValidation_Failure() {
        String claimId = "123";
        claimActivity.setFailValidation(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            claimActivity.validation(claimId);
        });

        assertEquals("Validation failed for testing purposes.", exception.getMessage());
    }

    @Test
    void testCompensateValidation() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.compensateValidation(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Validation Rolled Back", claim.getStatus());
    }

    // 2. Fraud Detection Tests
    @Test
    void testFraudDetection_Success() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.fraudDetection(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("No Fraud Detected", claim.getFraudStatus());
    }

    @Test
    void testFraudDetection_Failure() {
        String claimId = "123";
        claimActivity.setFailFraudDetection(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            claimActivity.fraudDetection(claimId);
        });

        assertEquals("Fraud detection failed for testing purposes.", exception.getMessage());
    }

    @Test
    void testCompensateFraudDetection() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.compensateFraudDetection(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Fraud Check Rolled Back", claim.getFraudStatus());
    }

    // 3. Approval Tests
    @Test
    void testApproval_Success() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.approval(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Approved", claim.getApprovalStatus());
    }

    @Test
    void testApproval_Failure() {
        String claimId = "123";
        claimActivity.setFailApproval(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            claimActivity.approval(claimId);
        });

        assertEquals("Approval failed for testing purposes.", exception.getMessage());
    }

    @Test
    void testCompensateApproval() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.compensateApproval(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Approval Rolled Back", claim.getApprovalStatus());
    }

    // 4. Payment Tests
//    @Test
//    void testPayment_Success() {
//        String claimId = "123";
//        Claim claim = createMockClaim(claimId);
//
//        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));
//
//        claimActivity.initiatePayment(claimId);
//
//        verify(claimRepository, times(1)).save(claim);
//        assertEquals("Payment Initiated", claim.getStatus());
//    }
//
    @Test
    void testPayment_Failure() {
        String claimId = "123";
        claimActivity.setFailPayment(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            claimActivity.initiatePayment(claimId);
        });

        assertEquals("Payment failed for testing purposes.", exception.getMessage());
    }

//    @Test
//    public void testPayment_RandomFailure() {
//        String claimId = "123";
//        Claim claim = createMockClaim(claimId);
//
//        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));
//
//        // Mock the getRandomValue() method to return a value that triggers the failure (e.g., less than 0.3)
//        claimActivity.setRandomValue(0.2);  // Simulate failure (random value < 0.3)
//
//        // Simulate random failure
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            claimActivity.initiatePayment(claimId);
//        });
//
//        assertEquals("Payment initiation failed", exception.getMessage());
//    }

    @Test
    void testCompensatePayment() {
        String claimId = "123";
        Claim claim = createMockClaim(claimId);

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(claim));

        claimActivity.compensatePayment(claimId);

        verify(claimRepository, times(1)).save(claim);
        assertEquals("Payment Compensation Triggered", claim.getStatus());
    }
}

