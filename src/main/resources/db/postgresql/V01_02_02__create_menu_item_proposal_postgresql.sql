INSERT INTO menu.menu (id, label) VALUES
(4, 'Funcionalidades');

INSERT INTO menu.menu_item (id, label, icon, id_menu) VALUES
(4, 'Propostas', 'pi pi-fw pi-briefcase', 4);

INSERT INTO menu.menu_item_router_link (id_menu_item, comand) VALUES
(4, '/pages/proposal');