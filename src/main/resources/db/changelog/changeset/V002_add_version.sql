--liquibase formatted sql
--changeset Svyatoslav:002
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'user_jwt'
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'card'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'card' AND column_name = 'version'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'user_jwt' AND column_name = 'version'

ALTER TABLE card
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE user_jwt
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

--rollback ALTER TABLE card DROP COLUMN IF EXISTS version;
--rollback ALTER TABLE user_jwt DROP COLUMN IF EXISTS version;