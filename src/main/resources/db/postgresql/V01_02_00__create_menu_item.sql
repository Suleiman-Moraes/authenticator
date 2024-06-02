INSERT INTO menu.menu (id, label) VALUES
(3, 'Protótipos');

INSERT INTO menu.menu_role (id_menu, role) VALUES
(3, 'ADMIN');

INSERT INTO menu.menu_item (id, label, icon, id_menu) VALUES
(3, 'Protótipo 1', 'pi pi-fw pi-wrench', 3);

INSERT INTO menu.menu_item_router_link (id_menu_item, comand) VALUES
(3, '/pages/prototype');

INSERT INTO menu.menu_item_role (id_menu_item, role) VALUES
(3, 'ADMIN');