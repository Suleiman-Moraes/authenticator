CREATE TABLE basic_token (
    id BIGSERIAL NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS basic_token
ADD
    CONSTRAINT unique_basic_token_username UNIQUE (username);