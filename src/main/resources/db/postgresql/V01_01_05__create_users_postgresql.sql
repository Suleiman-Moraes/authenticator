CREATE TABLE users (
    id BIGSERIAL NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_expired BOOLEAN,
    account_non_locked BOOLEAN,
    credentials_non_expired BOOLEAN,
    id_person BIGINT,
    id_profile BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS users
ADD
    CONSTRAINT unique_users_username UNIQUE (username);

ALTER TABLE
    IF EXISTS users
ADD
    CONSTRAINT fk_users_id_person FOREIGN KEY (id_person) REFERENCES person;

ALTER TABLE
    IF EXISTS users
ADD
    CONSTRAINT fk_users_id_profile FOREIGN KEY (id_profile) REFERENCES profile;