package com.example.ICPv3.exceptions;

public class NonRetryableClaimException extends RuntimeException {
    public NonRetryableClaimException(String message) {
        super(message);
    }
}