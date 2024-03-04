CREATE TABLE IF NOT EXISTS menu.question (
    id BIGSERIAL NOT NULL,
    value VARCHAR(255) NOT NULL,
    mask VARCHAR(50),
    type_from VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    order_question INTEGER NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS menu.question
ADD
    CONSTRAINT unique_question_type_from_value UNIQUE (type_from, value);

ALTER TABLE
    IF EXISTS menu.question
ADD
    CONSTRAINT unique_question_type_from_order UNIQUE (type_from, order_question);

CREATE TABLE IF NOT EXISTS menu.answer (
    id BIGSERIAL NOT NULL,
    value VARCHAR(255) NOT NULL,
    id_question BIGINT,
    id_person BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS menu.answer
ADD
    CONSTRAINT fk_answer_question_id FOREIGN KEY (id_question) REFERENCES menu.question;

ALTER TABLE
    IF EXISTS menu.answer
ADD
    CONSTRAINT fk_answer_person_id FOREIGN KEY (id_person) REFERENCES authenticator.person;

INSERT INTO
    menu.question (
        value,
        mask,
        type_from,
        type,
        order_question
    )
VALUES
    ('Telefone', '999 999 999', 'PERSON', 'MASK', 1);