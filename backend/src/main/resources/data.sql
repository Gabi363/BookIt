-- CREATE USERS
-- ADMIN
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'a.admin@email.com', 'Admin', 'Adminowski', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '111111111', 'ADMIN');
INSERT INTO admin_users (id) VALUES ((SELECT last_value FROM users_id_seq));
-- CLIENT
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'k.wybredny@email.com', 'Krzysztof', 'Wybredny', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '222222222', 'CLIENT');
INSERT INTO client_users (id) VALUES ((SELECT last_value FROM users_id_seq));
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'k.wymagajaca@email.com', 'Krystyna', 'Wymagająca', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '333333333', 'CLIENT');
INSERT INTO client_users (id) VALUES ((SELECT last_value FROM users_id_seq));
-- BUSINESS OWNER
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.biznesowski@email.com', 'Bartosz', 'Biznesowski', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '444444444', 'BUSINESS_OWNER');
INSERT INTO business_owner_users (id, nip, business_id) VALUES ((SELECT last_value FROM users_id_seq), '123456', null);
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.szef@email.com', 'Bartłomiej', 'Szef', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '555555555', 'BUSINESS_OWNER');
INSERT INTO business_owner_users (id, nip, business_id) VALUES ((SELECT last_value FROM users_id_seq), '234567', null);

-- CREATE BUSINESS
INSERT INTO business (id, contact_email, name, contact_phone, type) VALUES ((SELECT NEXTVAL('business_id_seq')), 's.nozyczki@email.com', 'Szalone Nożyczki', '222222222', 'HAIRSTYLE');
INSERT INTO business_address (business_id, city, local_number, post_code, street) VALUES ((SELECT last_value FROM business_id_seq), 'Kraków', '12/6', '12-345', 'Szalona');

INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'b.pracowita@email.com', 'Beata', 'Pracowita', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '666666666', 'WORKER');
INSERT INTO worker_users (id, business_id) VALUES ((SELECT last_value FROM users_id_seq), (SELECT last_value FROM business_id_seq));
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 's.porzadny@email.com', 'Sławomir', 'Porządny', '$2a$10$2cAmy3iHlhMUZNW9TsDCHeNhXZpw9g2RkMgv5SIYD.5AFpaL32Cia', '777777777', 'WORKER');
INSERT INTO worker_users (id, business_id) VALUES ((SELECT last_value FROM users_id_seq), (SELECT last_value FROM business_id_seq));

INSERT INTO service (id, description, service_name, price, business_id, category, duration) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Strzyżenie włosów długich, średnich i krótkich u kobiet', 'Strzyżenie damskie', 30, (SELECT last_value FROM business_id_seq), 'Strzyżenie damskie', 1.0);
INSERT INTO service (id, description, service_name, price, business_id, category, duration) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Strzyżenie męskie', 'Strzyżenie męskie', 25, (SELECT last_value FROM business_id_seq), 'Strzyżenie męskie', 1.5);


INSERT INTO business (id, contact_email, name, contact_phone, type) VALUES ((SELECT NEXTVAL('business_id_seq')), 'k.kredki@email.com', 'Kolorowe kredki', '333333333', 'MAKEUP');
INSERT INTO business_address (business_id, city, local_number, post_code, street) VALUES ((SELECT last_value FROM business_id_seq), 'Kraków', '15/3', '12-345', 'Kolorowa');
INSERT INTO users (id, email, first_name, last_name, password, phone_number, user_role) VALUES ((SELECT NEXTVAL('users_id_seq')), 'p.przykladny@email.com', 'Piotr', 'Przykładny', '$2a$10$gTvBOqQnIaY6pr6yhMaQ0OzvQAl2VFRF8Tm3CEfo0xl8h5MtgOp3W', '888888888', 'WORKER');
INSERT INTO worker_users (id) VALUES ((SELECT last_value FROM users_id_seq));
INSERT INTO service (id, description, service_name, price, business_id, category) VALUES ((SELECT NEXTVAL('service_id_seq')), 'Makijaż zwykły, na codzienne okazje.', 'Makjiaż codzienny', 80, (SELECT last_value FROM business_id_seq), 'Makijaż');

INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '20:00', true, '9:00', 'MONDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '20:00', true, '9:00', 'TUESDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '20:00', true, '9:00', 'WEDNESDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '20:00', true, '9:00', 'THURSDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '20:00', true, '9:00', 'FRIDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, end_time, is_open, start_time, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), '18:00', true, '10:00', 'SATURDAY', (SELECT last_value FROM business_id_seq));
INSERT INTO business_working_hours (id, is_open, week_day, business_id) VALUES ((SELECT nextval('business_working_hours_id_seq')), false, 'SUNDAY', (SELECT last_value FROM business_id_seq));


INSERT INTO ratings (id, comment, grade, client_id) VALUES ((SELECT nextval('ratings_id_seq')), 'Great saloon!', 5.0, 2);
INSERT INTO business_ratings (id, business_id) VALUES ((SELECT last_value FROM ratings_id_seq), (SELECT last_value FROM business_id_seq));
INSERT INTO ratings (id, comment, grade, client_id) VALUES ((SELECT nextval('ratings_id_seq')), 'Poor guy...', 2.0, 2);
INSERT INTO worker_ratings (id, worker_user_id) VALUES ((SELECT last_value FROM ratings_id_seq), 6);

INSERT INTO reservartions (id, date, business_id, client_user_id, service_id, worker_user_id) VALUES ((SELECT nextval('reservation_id_seq')), '2025-02-25', (SELECT last_value FROM business_id_seq), 2, (SELECT last_value FROM service_id_seq), 6);

INSERT INTO prizes (id, description, points_threshold, price, prize_name) VALUES ((SELECT nextval('prize_id_seq')),"Brawo! Pierwsze kroki za tobą. Trzymaj tak dalej!",30,20,"Pierwsze koty za płoty!");
INSERT INTO prizes (id, description, points_threshold, price, prize_name) VALUES ((SELECT nextval('prize_id_seq')),"Idzie ci coraz lepiej!",60,30,"Młody Padawan");
INSERT INTO prizes (id, description, points_threshold, price, prize_name) VALUES ((SELECT nextval('prize_id_seq')),"Już sporo za Tobą!",120,50,"Uczeń goni mistrza");
INSERT INTO prizes (id, description, points_threshold, price, prize_name) VALUES ((SELECT nextval('prize_id_seq')),"Można się od Ciebie uczyć!",200,70,"Zaawansowany gość");
INSERT INTO prizes (id, description, points_threshold, price, prize_name) VALUES ((SELECT nextval('prize_id_seq')),"Więcej chyba się nie da!",300,100,"Mistrz wziyt");

INSERT INTO points (id, points_number, update_date) VALUES ((SELECT nextval('points_id_seq')), 40,'2025-05-16');

INSERT INTO client_points (client_id) VALUES ((SELECT last_value FROM users_id_seq));

INSERT INTO user_prizes (id, used, prize_id, user_id, discount_code) VALUES ((SELECT nextval('user_prize_id_seq')), false, 1, (SELECT last_value FROM users_id_seq), 'dyegdwbkhwlo');
