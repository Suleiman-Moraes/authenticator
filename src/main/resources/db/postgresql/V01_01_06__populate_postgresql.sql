INSERT INTO company (name) VALUES ('Default Company');

INSERT INTO
    profile (description)
VALUES
    ('ROOT'),
    ('Admin'),
    ('Common user');

INSERT INTO
    profile_role (id_profile, role)
VALUES
    (1, 'ROOT'),
    (2, 'ADMIN'),
    (3, 'COMMON_USER');

INSERT INTO
    basic_token (username, enabled, password)
VALUES
    (
        'web',
        true,
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    );

INSERT INTO
    person (address, name, email)
VALUES
    ('Rua BA, Goiânia - GO', 'Root', 'suleimanmoraes@yahoo.com'),
    ('Rua AB, Goiânia - GO', 'Admin', 'suleimanmoraes@yahoo.com'),
    ('Rua ABC, Goiânia - GO', 'Suleiman Moraes', 'suleimanmoraes@yahoo.com');

INSERT INTO
    users (
        username,
        enabled,
        account_non_expired,
        account_non_locked,
        credentials_non_expired,
        id_person,
        id_profile,
        id_company,
        password
    )
VALUES
    (
        'root',
        true,
        true,
        true,
        true,
        1,
        1,
        1,
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    ),
    (
        'admin',
        true,
        true,
        true,
        true,
        2,
        2,
        1,
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    ),
    (
        'susu',
        true,
        true,
        true,
        true,
        3,
        3,
        1,
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    );