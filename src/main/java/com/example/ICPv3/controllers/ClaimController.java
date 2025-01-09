package com.example.ICPv3.controllers;


import com.example.ICPv3.models.Claim;
import com.example.ICPv3.services.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitClaim(@RequestBody Claim claim) {
        String result = claimService.submitClaim(claim);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> updateClaimStatus(@PathVariable String id, @RequestParam String status) {
        claimService.updateClaimStatus(id, status);
        return ResponseEntity.ok("Claim status updated to: " + status);
    }
}
