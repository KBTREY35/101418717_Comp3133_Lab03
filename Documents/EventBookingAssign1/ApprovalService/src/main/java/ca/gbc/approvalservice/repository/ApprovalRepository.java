package ca.gbc.approvalservice.repository;

import ca.gbc.approvalservice.entity.Approval;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApprovalRepository extends MongoRepository<Approval, String> {

}
