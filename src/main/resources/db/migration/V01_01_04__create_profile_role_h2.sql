CREATE TABLE profile_role (
    id_profile bigint NOT NULL,
    `role` varchar(50)
);

ALTER TABLE
    IF EXISTS profile_role
ADD
    CONSTRAINT fk_profile_role_profile_id FOREIGN KEY (id_profile) REFERENCES PROFILE;