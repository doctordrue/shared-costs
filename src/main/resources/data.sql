INSERT INTO currency (short_name, full_name, rate) VALUES ('RUR', 'Российский Рубль', 1.0);
INSERT INTO currency (short_name, full_name, rate) VALUES ('EUR', 'Euro', 130.0);
INSERT INTO currency (short_name, full_name, rate) VALUES ('USD', 'US Dollar', 110.0);

INSERT INTO persons (first_name, last_name, phone_number) VALUES ('Роман', 'К.', '1-(235)314-9823');
INSERT INTO persons (first_name, last_name, phone_number) VALUES ('Андрей', 'Б.', '1-(235)314-9823');
INSERT INTO persons (first_name, last_name, phone_number) VALUES ('Саша', 'С.', '1-(235)314-9823');
INSERT INTO persons (first_name, last_name, phone_number) VALUES ('Дима', 'Х.', '1-(235)314-9823');
INSERT INTO persons (first_name, last_name, phone_number) VALUES ('Миша', 'А.', '1-(235)314-9823');

INSERT INTO costs_groups (name, description, start_date) VALUES ('Игра в настолки', 'У Романа', '2022-03-12');

INSERT INTO costs (name, group_id, currency_id, cost_total, cost_datetime)
VALUES ('Единорожики', (SELECT id FROM costs_groups WHERE name='Игра в настолки'), (SELECT id from currency WHERE short_name='RUR'), 990.0, '2022-03-12 20:00:00');
INSERT INTO costs (name, group_id, currency_id, cost_total, cost_datetime)
VALUES ('Японская игра', (SELECT id FROM costs_groups WHERE name='Игра в настолки'), (SELECT id from currency WHERE short_name='EUR'), 10.0, '2022-03-12 20:01:00');

INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Роман'), (SELECT cost_total FROM costs WHERE name='Единорожики')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Андрей'), (SELECT cost_total FROM costs WHERE name='Единорожики')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Саша'), (SELECT cost_total FROM costs WHERE name='Единорожики')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Дима'), (SELECT cost_total FROM costs WHERE name='Единорожики')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Миша'), (SELECT cost_total FROM costs WHERE name='Единорожики')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Роман'), (SELECT cost_total FROM costs WHERE name='Японская игра')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Андрей'), (SELECT cost_total FROM costs WHERE name='Японская игра')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Саша'), (SELECT cost_total FROM costs WHERE name='Японская игра')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Дима'), (SELECT cost_total FROM costs WHERE name='Японская игра')/5);
INSERT INTO stakes (cost_id, person_id, stake_total)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Миша'), (SELECT cost_total FROM costs WHERE name='Японская игра')/5);

INSERT INTO payments (cost_id, person_id, payment_total, payment_datetime)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Саша'), 495.0, '2022-03-12 20:00:00');
INSERT INTO payments (cost_id, person_id, payment_total, payment_datetime)
VALUES ((SELECT id FROM costs WHERE name='Единорожики'), (SELECT id FROM persons WHERE first_name='Андрей'), 495.0, '2022-03-12 20:00:00');
INSERT INTO payments (cost_id, person_id, payment_total, payment_datetime)
VALUES ((SELECT id FROM costs WHERE name='Японская игра'), (SELECT id FROM persons WHERE first_name='Роман'), 10.0, '2022-03-12 20:00:00');
