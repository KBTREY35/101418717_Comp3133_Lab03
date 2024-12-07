CREATE TABLE approvals (
                           id BIGSERIAL PRIMARY KEY,
                           event_id BIGINT NOT NULL,
                           approver_id BIGINT NOT NULL,
                           status VARCHAR(255) NOT NULL
);

-- V1 FILE APPROVAL
