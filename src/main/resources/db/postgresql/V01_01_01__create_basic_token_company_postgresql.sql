CREATE SCHEMA IF NOT EXISTS authenticator;

CREATE TABLE IF NOT EXISTS basic_token (
    id BIGSERIAL NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS company (
    id BIGSERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS basic_token
ADD
    CONSTRAINT unique_basic_token_username UNIQUE (username);