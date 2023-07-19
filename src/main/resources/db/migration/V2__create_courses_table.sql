CREATE TABLE courses(
    id UUID NOT NULL,
    name VARCHAR NOT NULL,
    slug VARCHAR NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    professor_id UUID NOT NULL,
    CONSTRAINT courses_pk PRIMARY KEY (id),
    CONSTRAINT users_fk FOREIGN KEY (professor_id) REFERENCES users(id)
)