package com.example.ICPv3.workflows;

import com.example.ICPv3.repositories.ClaimRepository;
import io.temporal.client.WorkflowClient;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.springframework.stereotype.Component;


@WorkflowInterface
public interface InsuranceClaimWorkflow {

    @WorkflowMethod
    void processClaim(String claimId);

    @SignalMethod
    void updateClaimDetails(String claimId, String newStatus);

    @QueryMethod
    String getCurrentClaimStatus();
}
