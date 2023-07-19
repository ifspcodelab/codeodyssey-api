CREATE TABLE courses(
    id UUID,
    name VARCHAR,
    slug VARCHAR NOT NULL UNIQUE,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMPTZ,
    professor_id UUID,
    CONSTRAINT courses_pk PRIMARY KEY (id),
    CONSTRAINT users_fk FOREIGN KEY (professor_id) REFERENCES users(id)
)