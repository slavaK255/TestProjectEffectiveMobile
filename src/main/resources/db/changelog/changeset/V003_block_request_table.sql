--liquibase formatted sql
--changeset Svyatoslav:003
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'block_request'

CREATE TABLE block_request (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    card_id BIGINT NOT NULL,
    reason VARCHAR(4096) NOT NULL,
    executed BOOLEAN NOT NULL DEFAULT false
);

--rollback DROP TABLE IF EXISTS block_request;
