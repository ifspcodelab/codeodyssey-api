CREATE TABLE users(
    id UUID,
    name CHAR(100),
    email CHAR(350) NOT NULL UNIQUE,
    password CHAR(64),
    role VARCHAR,
    created_at TIMESTAMP,
    CONSTRAINT users_pk PRIMARY KEY (id)
)