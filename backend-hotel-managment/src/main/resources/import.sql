INSERT INTO bed_types(type) VALUE('Matrimoniales');
INSERT INTO bed_types(type) VALUE('King size');

INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación estándar', 1000, 'Las habitaciones Estándar estan disponibles en dos camas matrimoniales o cama king size.', 1);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación estándar', 1000, 'Las habitaciones Estándar estan disponibles en dos camas matrimoniales o cama king size.', 2);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación estándar con terraza', 1300, 'Las habitaciones Estándar con terraza estan disponibles en dos camas matrimoniales o cama king size.', 1);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación estándar con terraza', 1300, 'Las habitaciones Estándar con terraza estan disponibles en dos camas matrimoniales o cama king size.', 2);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación vista al mar', 1500, 'Las habitaciones Vista al Mar estan disponibles en dos camas matrimoniales o cama king size, cuenta con terraza.', 1);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación vista al mar', 1500, 'Las habitaciones Vista al Mar estan disponibles en dos camas matrimoniales o cama king size, cuenta con terraza.', 2);
INSERT INTO rooms(room_type, price, description, id_bed_type) VALUES('Habitación frente al mar', 1700, 'Las habitaciones Frente al Mar son ideales para pareja, disponibles unicamente con cama king size.', 2 );

INSERT INTO payment_methods(payment) VALUE ('Efectivo');
INSERT INTO payment_methods(payment) VALUE ('Tarjeta');
INSERT INTO payment_methods(payment) VALUE ('Transferencia');

INSERT INTO social_networks(social_network) VALUE('Facebook');
INSERT INTO social_networks(social_network) VALUE('Instagram');
INSERT INTO social_networks(social_network) VALUE('Twitter');
INSERT INTO social_networks(social_network) VALUE('Tiktok');

INSERT INTO app_users(name, last_name, email, creation_date, password, enabled) VALUES('Samir', 'De la torre', 'samirdelatorre@gmail.com', '2022-05-16', '$2a$10$s10tOlD4s8VcboyIpmBnkOZxDX7MK4pKfMD/LJNs/g7IXSArZcCY6', 1);

INSERT INTO roles(role) VALUE('ROLE_USER');
INSERT INTO roles(role) VALUE('ROLE_ADMIN');

INSERT INTO rel_users_roles(user_id, role_id) VALUES(1, 1);
INSERT INTO rel_users_roles(user_id, role_id) VALUES(1, 2);
