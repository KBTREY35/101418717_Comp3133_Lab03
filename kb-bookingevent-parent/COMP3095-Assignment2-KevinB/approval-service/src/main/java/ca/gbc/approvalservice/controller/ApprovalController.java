package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApprovalResponse> createApproval(@RequestBody ApprovalRequest approvalRequest) {
        ApprovalResponse createdApproval = approvalService.createApproval(approvalRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/approvals/" + createdApproval.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdApproval);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApprovalResponse> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<ApprovalResponse> getApprovalById(@PathVariable("approvalId") Long approvalId) {
        ApprovalResponse approval = approvalService.getApprovalById(approvalId);
        return ResponseEntity.ok(approval);
    }

    @PutMapping("/{approvalId}")
    public ResponseEntity<Void> updateApproval(@PathVariable("approvalId") Long approvalId,
                                               @RequestBody ApprovalRequest approvalRequest) {
        approvalService.updateApproval(approvalId, approvalRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/approvals/" + approvalId);

        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<Void> deleteApproval(@PathVariable("approvalId") Long approvalId) {
        approvalService.deleteApproval(approvalId);
        return ResponseEntity.noContent().build();
    }

    // New endpoint to fetch user roles
    @GetMapping("/get-user-roles/{userId}")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable Long userId) {
        List<String> roles = approvalService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}
