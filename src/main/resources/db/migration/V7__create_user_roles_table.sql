CREATE TABLE user_roles(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles(id)
)