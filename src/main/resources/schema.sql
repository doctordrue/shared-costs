create table currency(
    id BIGSERIAL PRIMARY KEY,
    short_name VARCHAR(4) UNIQUE NOT NULL,
    full_name VARCHAR(32),
    -- rate is how many RUBs the currency costs
    rate DECIMAL NOT NULL
);

CREATE TABLE persons(
-- list of people available to be included in cost groups etc.
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR UNIQUE,
  password VARCHAR,
  first_name VARCHAR(64),
  last_name VARCHAR(64),
  phone_number VARCHAR(24)
);

CREATE TABLE costs_groups(
-- cost groups such as trips or parties.
-- always have the constant number of people to share all costs between
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(256),
  start_date DATE NOT NULL,
  end_date DATE
);

create table costs(
-- this is a particular cost which need to be shared between people in cost group, e.g. bill in cafe etc.
    id BIGSERIAL PRIMARY KEY,
    name varchar(32) NOT NULL,
    group_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    cost_total decimal(10, 2) NOT NULL,
    cost_datetime DATETIME NOT NULL
);

create table stakes(
-- How much money exact person should have been spend for the particular cost.
-- The currency is the same as in the cost
    id BIGSERIAL PRIMARY KEY,
    cost_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    stake_total decimal(10, 2) NOT NULL
);

create table payments(
-- this table stores who has made real payment to cover the cost price
-- The currency is the same as in the cost
    id BIGSERIAL PRIMARY KEY,
    cost_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    payment_total decimal(10, 2) NOT NULL,
    payment_datetime DATETIME NOT NULL
);

ALTER TABLE costs ADD FOREIGN KEY (currency_id) REFERENCES currency(id);
ALTER TABLE costs ADD FOREIGN KEY (group_id) REFERENCES costs_groups(id);
ALTER TABLE payments ADD FOREIGN KEY (cost_id) REFERENCES costs(id);
ALTER TABLE payments ADD FOREIGN KEY (person_id) REFERENCES persons(id);
ALTER TABLE stakes ADD FOREIGN KEY (person_id) REFERENCES persons(id);
ALTER TABLE stakes ADD FOREIGN KEY (cost_id) REFERENCES costs(id);