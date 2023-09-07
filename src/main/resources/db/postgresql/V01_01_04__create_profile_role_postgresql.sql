CREATE TABLE profile_role (
    id_profile BIGINT NOT NULL,
    role VARCHAR(255) CHECK (ROLE IN ('ROOT', 'ADMIN', 'COMMON_USER'))
);

ALTER TABLE
    IF EXISTS profile_role
ADD
    CONSTRAINT fk_profile_role_profile_id FOREIGN KEY (id_profile) REFERENCES PROFILE;