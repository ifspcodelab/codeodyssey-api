CREATE TABLE users(
    id UUID,
    email VARCHAR(350) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password CHAR(60) NOT NULL,
    role VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_validated BOOLEAN NOT NULL,
    token VARCHAR NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
)
