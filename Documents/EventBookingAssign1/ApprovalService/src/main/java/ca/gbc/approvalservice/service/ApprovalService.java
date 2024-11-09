package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.entity.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalService {
    @Autowired
    private ApprovalRepository approvalRepository;

    public Approval createApproval(Approval approval) {
        return approvalRepository.save(approval);
    }

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public Approval getApprovalById(String id) {
        return approvalRepository.findById(id).orElse(null);
    }

    public Approval updateApproval(Approval approval) {
        return approvalRepository.save(approval);
    }

    public void deleteApproval(String id) {
        approvalRepository.deleteById(id);
    }
}
