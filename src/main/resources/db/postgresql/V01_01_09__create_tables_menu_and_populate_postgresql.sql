CREATE SCHEMA IF NOT EXISTS menu;

CREATE TABLE IF NOT EXISTS menu.menu (
    id BIGSERIAL NOT NULL,
    label VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS menu.menu_role (
    id_menu BIGINT NOT NULL,
    role VARCHAR(50)
);

ALTER TABLE
    IF EXISTS menu.menu_role
ADD
    CONSTRAINT fk_menu_role_menu_id FOREIGN KEY (id_menu) REFERENCES menu.menu;


CREATE TABLE IF NOT EXISTS menu.menu_item (
    id BIGSERIAL NOT NULL,
    label VARCHAR(50) NOT NULL,
    icon VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    id_menu BIGINT,
    id_menu_item BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE
    IF EXISTS menu.menu_item
ADD
    CONSTRAINT fk_menu_item_menu_item_id FOREIGN KEY (id_menu_item) REFERENCES menu.menu_item;

ALTER TABLE
    IF EXISTS menu.menu_item
ADD
    CONSTRAINT fk_menu_item_menu_id FOREIGN KEY (id_menu) REFERENCES menu.menu;

CREATE TABLE IF NOT EXISTS menu.menu_item_role (
    id_menu_item BIGINT NOT NULL,
    role VARCHAR(50)
);

ALTER TABLE
    IF EXISTS menu.menu_item_role
ADD
    CONSTRAINT fk_menu_item_role_menu_item_id FOREIGN KEY (id_menu_item) REFERENCES menu.menu_item;

CREATE TABLE IF NOT EXISTS menu.menu_item_router_link (
    id_menu_item BIGINT NOT NULL,
    comand VARCHAR(50)
);

ALTER TABLE
    IF EXISTS menu.menu_item_router_link
ADD
    CONSTRAINT fk_menu_item_router_link_menu_item_id FOREIGN KEY (id_menu_item) REFERENCES menu.menu_item;

INSERT INTO menu.menu (id, label) VALUES
(1, 'Home'),
(2, 'Administração');

INSERT INTO menu.menu_role (id_menu, role) VALUES
(2, 'ADMIN');

INSERT INTO menu.menu_item (id, label, icon, id_menu) VALUES
(1, 'Dashboard', 'pi pi-fw pi-home', 1),
(2, 'Pessoas', 'pi pi-fw pi-id-card', 2);

INSERT INTO menu.menu_item_router_link (id_menu_item, comand) VALUES
(1, '/pages'),
(2, '/pages/person');

INSERT INTO menu.menu_item_role (id_menu_item, role) VALUES
(2, 'ADMIN');