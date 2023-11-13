CREATE TABLE invitations(
    id UUID NOT NULL,
    link CHAR(49) NOT NULL,
    expiration_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    course_id UUID NOT NULL,
    is_active BOOLEAN NOT NULL,
    CONSTRAINT invitations_pk PRIMARY KEY (id),
    CONSTRAINT courses_fk FOREIGN KEY (course_id) REFERENCES courses(id)
)
