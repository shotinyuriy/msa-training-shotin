CREATE TYPE IF NOT EXISTS address_type (
    street VARCHAR,
    city VARCHAR,
    state VARCHAR);
    |
CREATE TYPE IF NOT EXISTS bank_account_type (
    uuid UUID,
    first_name VARCHAR,
    last_name VARCHAR,
    patronymic VARCHAR,
    account_number BIGINT,
    account_type VARCHAR
);