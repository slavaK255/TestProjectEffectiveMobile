--liquibase formatted sql
--changeset Svyatoslav:001
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'user_jwt'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'card'

CREATE TABLE user_jwt (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    login varchar(64) UNIQUE NOT NULL,
    user_name varchar (128) NOT NULL,
    password varchar(128) NOT NULL,
    role varchar(64) NOT NULL
);

CREATE TABLE card (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    card_number VARCHAR(16) NOT NULL CHECK (LENGTH(card_number) = 16),
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMPTZ NOT NULL,
    status VARCHAR(64) NOT NULL DEFAULT 'ACTIVE',
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_jwt (id)
);

--rollback DROP TABLE IF EXISTS user_jwt;
--rollback DROP TABLE IF EXISTS card;