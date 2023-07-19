CREATE TABLE users(
    id UUID,
    name VARCHAR,
    email VARCHAR NOT NULL UNIQUE,
    password VARCHAR,
    role VARCHAR,
    created_at TIMESTAMPTZ,
    CONSTRAINT users_pk PRIMARY KEY (id)
)