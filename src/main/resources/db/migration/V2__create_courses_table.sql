CREATE TABLE courses(
    id UUID,
    name CHAR(255),
    slug CHAR(255) NOT NULL UNIQUE,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP,
    professor_id UUID,
    CONSTRAINT courses_pk PRIMARY KEY (id),
    CONSTRAINT users_fk FOREIGN KEY (professor_id) REFERENCES users(id)
)