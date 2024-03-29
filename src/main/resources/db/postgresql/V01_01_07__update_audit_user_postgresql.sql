ALTER TABLE
    IF EXISTS users
ADD
    COLUMN created_by VARCHAR(50);

ALTER TABLE
    IF EXISTS users
ADD
    COLUMN created_date TIMESTAMP(6) WITH TIME ZONE;

ALTER TABLE
    IF EXISTS users
ADD
    COLUMN last_modified_by VARCHAR(50);

ALTER TABLE
    IF EXISTS users
ADD
    COLUMN last_modified_date TIMESTAMP(6) WITH TIME ZONE;

CREATE TABLE person_aud (
    id BIGSERIAL NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    address VARCHAR(255),
    name VARCHAR(150),
    email VARCHAR(150),
    PRIMARY KEY (rev, id)
);

CREATE TABLE revinfo (
    rev INTEGER generated by default as identity,
    revtstmp BIGINT,
    PRIMARY KEY (rev)
);

CREATE TABLE users_aud (
    id BIGSERIAL NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    account_non_expired BOOLEAN,
    account_non_locked BOOLEAN,
    credentials_non_expired BOOLEAN,
    enabled BOOLEAN,
    token_reset_password_enabled BOOLEAN,
    password VARCHAR(255),
    username VARCHAR(50),
    token_reset_password VARCHAR(36),
    token_reset_password_expiration_date TIMESTAMP,
    id_person BIGINT,
    PRIMARY KEY (rev, id)
);

CREATE SEQUENCE revinfo_seq START WITH 1 INCREMENT BY 50;

ALTER TABLE
    IF EXISTS person_aud
ADD
    CONSTRAINT fk_person_aud_rev FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE
    IF EXISTS users_aud
ADD
    CONSTRAINT fk_users_aud_rev FOREIGN KEY (rev) REFERENCES revinfo;