CREATE TABLE user_jwt (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    login varchar(64) UNIQUE NOT NULL,
    password varchar(128) NOT NULL,
    role varchar(64) NOT NULL
);