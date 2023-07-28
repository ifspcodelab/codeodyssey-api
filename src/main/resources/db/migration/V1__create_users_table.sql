CREATE TABLE users(
    id UUID,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    role VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_validated BOOLEAN NOT NULL,
    token VARCHAR NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
)
