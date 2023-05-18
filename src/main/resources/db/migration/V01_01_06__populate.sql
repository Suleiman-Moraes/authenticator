INSERT INTO
    `profile` (`description`)
VALUES
    ('ROOT'),
    ('Admin'),
    ('Common user');

INSERT INTO
    `profile_role` (`id_profile`, `role`)
VALUES
    (1, 'ROOT'),
    (1, 'ADMIN'),
    (1, 'COMMON_USER'),
    (2, 'ADMIN'),
    (3, 'COMMON_USER');

INSERT INTO
    `basic_token` (`username`, `enabled`, `password`)
VALUES
    (
        'web',
        true,
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    );

INSERT INTO
    `person` (`address`, `name`)
VALUES
    ('Rua BA, Goiânia - GO', 'Root'),
    ('Rua AB, Goiânia - GO', 'Admin'),
    ('Rua ABC, Goiânia - GO', 'Suleiman Moraes');

INSERT INTO
    `users` (
        `username`,
        `enabled`,
        `account_non_expired`,
        `account_non_locked`,
        `credentials_non_expired`,
        `id_person`,
        `id_profile`,
        `password`
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
        '{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5'
    );