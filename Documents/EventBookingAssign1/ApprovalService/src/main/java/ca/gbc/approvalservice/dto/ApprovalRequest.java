package ca.gbc.approvalservice.dto;

public record ApprovalRequest(
        Long eventId,
        Long approverId,
        String status
) {
}
