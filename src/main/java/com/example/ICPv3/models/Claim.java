package com.example.ICPv3.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Setter
@Getter
@Document(collection = "claims")
public class Claim {
    @Id
    private String id;
    private String policyHolderName;
    private String claimType;
    private String status;
    private double claimAmount;
    private String fraudStatus;
    private String approvalStatus;
    private String paymentStatus;

}

