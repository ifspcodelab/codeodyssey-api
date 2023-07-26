CREATE TABLE users(
    id UUID,
    email VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    password CHAR(60) NOT NULL,
    role VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
)
