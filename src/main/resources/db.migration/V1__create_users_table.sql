CREATE TABLE users (
    id UUID NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    role UserRole NOT NULL,
    createdAt Instant NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
)