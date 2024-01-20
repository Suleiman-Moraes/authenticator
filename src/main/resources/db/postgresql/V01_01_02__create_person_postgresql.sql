CREATE TABLE person (
    id BIGSERIAL NOT NULL,
    address VARCHAR(255),
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (id)
);