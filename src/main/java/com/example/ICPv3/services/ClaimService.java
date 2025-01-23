package com.example.ICPv3.services;

import com.example.ICPv3.models.Claim;
import com.example.ICPv3.repositories.ClaimRepository;
import com.example.ICPv3.workflows.InsuranceClaimWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {

    @Autowired
    private  ClaimRepository claimRepository;
    @Autowired
    private  WorkflowClient workflowClient;


    public String submitClaim(Claim claim) {
        // Save the claim to MongoDB
        claim.setStatus("Submitted");
        claimRepository.save(claim);

        // Trigger the Temporal workflow
        InsuranceClaimWorkflow workflow = workflowClient.newWorkflowStub(
                InsuranceClaimWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("ClaimTaskQueue")
                        .build());

        // Start the workflow with the claim ID
        WorkflowClient.start(workflow::processClaim, claim.getId());

        return "Claim submitted and workflow started for claim ID: " + claim.getId();
    }

    public void updateClaimStatus(String claimId, String status) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
        claim.setStatus(status);
        claimRepository.save(claim);
    }
}
