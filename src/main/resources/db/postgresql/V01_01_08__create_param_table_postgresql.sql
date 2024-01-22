CREATE TABLE IF NOT EXISTS param (
    id BIGSERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    value VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS param
ADD
    CONSTRAINT unique_param_name UNIQUE (name);