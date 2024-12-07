package ca.gbc.approvalservice.dto;

public record ApprovalResponse(
        Long id,
        Long eventId,
        Long approverId,
        String status
) {
}
