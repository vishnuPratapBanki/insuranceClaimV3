package com.example.ICPv3.config;

import com.example.ICPv3.activities.ClaimActivityImpl;
import com.example.ICPv3.workflows.InsuranceClaimWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker claimWorker(WorkerFactory factory, ClaimActivityImpl activities) {
        Worker worker = factory.newWorker("ClaimTaskQueue");
        worker.registerWorkflowImplementationTypes(InsuranceClaimWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities); // Use Spring-managed bean
        factory.start();
        return worker;
    }
}