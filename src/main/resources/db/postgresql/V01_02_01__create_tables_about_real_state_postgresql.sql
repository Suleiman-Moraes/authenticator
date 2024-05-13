CREATE SCHEMA IF NOT EXISTS real_state;

CREATE TABLE IF NOT EXISTS real_state.construction (
    id BIGSERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    id_company BIGINT NOT NULL,
    created_by BIGINT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by BIGINT NULL,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS real_state.construction
ADD
    CONSTRAINT fk_construction_company_id FOREIGN KEY (id_company) REFERENCES authenticator.company;

ALTER TABLE
    IF EXISTS real_state.construction
ADD
    CONSTRAINT fk_construction_created_by FOREIGN KEY (created_by) REFERENCES authenticator.users;

ALTER TABLE
    IF EXISTS real_state.construction
ADD
    CONSTRAINT fk_construction_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES authenticator.users;

ALTER TABLE
    IF EXISTS real_state.construction
ADD
    CONSTRAINT unique_construction_name UNIQUE (name, id_company);

-- enterprise

CREATE TABLE IF NOT EXISTS real_state.enterprise (
    id BIGSERIAL NOT NULL,
    value NUMERIC(10, 2) NOT NULL,
    vpl NUMERIC(10, 2) NULL,
    valuem2 NUMERIC(10, 2) NOT NULL,
    sizem2 NUMERIC(10, 2) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    id_construction BIGINT NOT NULL,
    created_by BIGINT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by BIGINT NULL,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS real_state.enterprise
ADD
    CONSTRAINT fk_enterprise_construction_id FOREIGN KEY (id_construction) REFERENCES real_state.construction;

ALTER TABLE
    IF EXISTS real_state.enterprise
ADD
    CONSTRAINT fk_enterprise_created_by FOREIGN KEY (created_by) REFERENCES authenticator.users;

ALTER TABLE
    IF EXISTS real_state.enterprise
ADD
    CONSTRAINT fk_enterprise_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES authenticator.users;

-- proposal

CREATE TABLE IF NOT EXISTS real_state.proposal (
    id BIGSERIAL NOT NULL,
    value NUMERIC(10, 2) NOT NULL,
    vpl NUMERIC(10, 2) NULL,
    valuem2 NUMERIC(10, 2) NOT NULL,
    sizem2 NUMERIC(10, 2) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    id_enterprise BIGINT NOT NULL,
    created_by BIGINT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by BIGINT NULL,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS real_state.proposal
ADD
    CONSTRAINT fk_proposal_enterprise_id FOREIGN KEY (id_enterprise) REFERENCES real_state.enterprise;

ALTER TABLE
    IF EXISTS real_state.proposal
ADD
    CONSTRAINT fk_proposal_created_by FOREIGN KEY (created_by) REFERENCES authenticator.users;

ALTER TABLE
    IF EXISTS real_state.proposal
ADD
    CONSTRAINT fk_proposal_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES authenticator.users;

-- condition

CREATE TABLE IF NOT EXISTS real_state.condition (
    id BIGSERIAL NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    number_installments INTEGER NOT NULL,
    value_installments NUMERIC(10, 2) NULL,
    amount NUMERIC(10, 2) NOT NULL,
    beginning_installment TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    id_proposal BIGINT NOT NULL,
    created_by BIGINT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by BIGINT NULL,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS real_state.condition
ADD
    CONSTRAINT fk_condition_proposal_id FOREIGN KEY (id_proposal) REFERENCES real_state.proposal;

ALTER TABLE
    IF EXISTS real_state.condition
ADD
    CONSTRAINT fk_condition_created_by FOREIGN KEY (created_by) REFERENCES authenticator.users;

ALTER TABLE
    IF EXISTS real_state.condition
ADD
    CONSTRAINT fk_condition_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES authenticator.users;