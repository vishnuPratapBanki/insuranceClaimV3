package com.example.ICPv3.workflows;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface InsuranceClaimWorkflow {
    @WorkflowMethod
    void processClaim(String claimId);

    @SignalMethod
    void updateClaimDetails(String claimId, String newStatus);

    @QueryMethod
    String getCurrentClaimStatus();
}
