CREATE TABLE refreshtokens(
    id UUID NOT NULL,
    expiry_at TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT refreshtokens_pk PRIMARY KEY (id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)
