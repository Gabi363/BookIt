-- CREATE USERS
-- ADMIN
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'a.aaaaaaaaaa@email.com', 'Admin', 'Adminowski', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '111111111', 'ADMIN');
INSERT INTO admin_users (id) VALUES ((SELECT last_value FROM users_id_seq));
-- CLIENT
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'k.wybredny@email.com', 'Krzysztof', 'Wybredny', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '222222222', 'CLIENT');
INSERT INTO client_users (id) VALUES ((SELECT last_value FROM users_id_seq));
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'k.wymagajaca@email.com', 'Krystyna', 'Wymagająca', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '333333333', 'CLIENT');
INSERT INTO client_users (id) VALUES ((SELECT last_value FROM users_id_seq));
-- BUSINESS OWNER
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.biznesowski@email.com', 'Bartosz', 'Biznesowski', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '444444444', 'BUSINESS_OWNER');
INSERT INTO business_owner_users (id, nip, business_id) VALUES ((SELECT last_value FROM users_id_seq), '123456', null);
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.szef@email.com', 'Bartłomiej', 'Szef', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '555555555', 'BUSINESS_OWNER');
INSERT INTO business_owner_users (id, nip, business_id) VALUES ((SELECT last_value FROM users_id_seq), '234567', null);

-- CREATE BUSINESS
INSERT INTO business (id, contact_email, name, contact_phone, type) VALUES ((SELECT NEXTVAL('business_id_seq')), 's.nozyczki@email.com', 'Szalone Nożyczki', '222222222', 'HAIRSTYLE');
INSERT INTO business_address (business_id, city, local_number, post_code, street) VALUES ((SELECT last_value FROM business_id_seq), 'Kraków', '12/6', '12-345', 'Szalona');

INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.pracowita@email.com', 'Beata', 'Pracowita', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '666666666', 'WORKER');
INSERT INTO worker_users (id, business_id) VALUES ((SELECT last_value FROM users_id_seq), (SELECT last_value FROM business_id_seq));
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 's.porzadny@email.com', 'Sławomir', 'Porządny', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '777777777', 'WORKER');
INSERT INTO worker_users (id, business_id) VALUES ((SELECT last_value FROM users_id_seq), (SELECT last_value FROM business_id_seq));

INSERT INTO service (id, description, service_name, price, business_id, category) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Strzyżenie włosów długich, średnich i krótkich u kobiet', 'Strzyżenie damskie', 30, (SELECT last_value FROM business_id_seq), 'Strzyżenie damskie');
INSERT INTO service (id, description, service_name, price, business_id, category) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Strzyżenie męskie', 'Strzyżenie męskie', 25, (SELECT last_value FROM business_id_seq), 'Strzyżenie męskie');


INSERT INTO business (id, contact_email, name, contact_phone, type) VALUES ((SELECT NEXTVAL('business_id_seq')), 'k.kredki@email.com', 'Kolorowe kredki', '333333333', 'MAKEUP');
INSERT INTO business_address (business_id, city, local_number, post_code, street) VALUES ((SELECT last_value FROM business_id_seq), 'Kraków', '15/3', '12-345', 'Kolorowa');
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'p.przykladny@email.com', 'Piotr', 'Przykładny', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '888888888', 'WORKER');
INSERT INTO worker_users (id) VALUES ((SELECT last_value FROM users_id_seq));
INSERT INTO service (id, description, service_name, price, business_id, category) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Makijaż zwykły, na codzienne okazje.', 'Makjiaż codzienny', 80, (SELECT last_value FROM business_id_seq), 'Makijaż');

--
