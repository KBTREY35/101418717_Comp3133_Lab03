package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.client.UserServiceClient;
import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {
        log.debug("Creating a new approval for event ID {}", approvalRequest.eventId());

        Approval approval = Approval.builder()
                .eventId(approvalRequest.eventId())
                .approverId(approvalRequest.approverId())
                .status(approvalRequest.status())
                .build();

        Approval savedApproval = approvalRepository.save(approval);
        log.info("Approval {} is saved", savedApproval.getId());

        return mapToApprovalResponse(savedApproval);
    }

    @Override
    public List<ApprovalResponse> getAllApprovals() {
        log.debug("Getting all approvals");

        List<Approval> approvals = approvalRepository.findAll();
        return approvals.stream().map(this::mapToApprovalResponse).toList();
    }

    @Override
    public ApprovalResponse getApprovalById(Long approvalId) {
        log.debug("Fetching approval with ID {}", approvalId);
        Approval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval not found"));
        return mapToApprovalResponse(approval);
    }

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserRoles")
    public List<String> getUserRoles(Long userId) {
        log.info("Fetching roles for user with ID {}", userId);
        return userServiceClient.getUserRoles(userId);
    }

    public List<String> fallbackGetUserRoles(Long userId, Throwable throwable) {
        log.warn("Fallback executed for UserService: {}", throwable.getMessage());
        return List.of("default-role"); // Return a default role in case of failure
    }

    @Override
    public void updateApproval(Long approvalId, ApprovalRequest approvalRequest) {
        log.debug("Updating approval with ID {}", approvalId);

        Approval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval not found"));
        approval.setEventId(approvalRequest.eventId());
        approval.setApproverId(approvalRequest.approverId());
        approval.setStatus(approvalRequest.status());

        approvalRepository.save(approval);
    }

    @Override
    public void deleteApproval(Long approvalId) {
        log.debug("Deleting approval with ID {}", approvalId);
        approvalRepository.deleteById(approvalId);
    }

    private ApprovalResponse mapToApprovalResponse(Approval approval) {
        return new ApprovalResponse(
                approval.getId(),
                approval.getEventId(),
                approval.getApproverId(),
                approval.getStatus()
        );
    }
}
