package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.entity.Approval;
import ca.gbc.approvalservice.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping
    public Approval createApproval(@RequestBody Approval approval) {
        return approvalService.createApproval(approval);
    }

    @GetMapping
    public List<Approval> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Approval> getApprovalById(@PathVariable String id) {
        Approval approval = approvalService.getApprovalById(id);
        return approval == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(approval);
    }

    @PutMapping("/{id}")
    public Approval updateApproval(@PathVariable String id, @RequestBody Approval approval) {
        approval.setId(id);
        return approvalService.updateApproval(approval);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(@PathVariable String id) {
        approvalService.deleteApproval(id);
        return ResponseEntity.noContent().build();
    }
}
