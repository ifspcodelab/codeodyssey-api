CREATE TABLE activities(
    id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    course_id UUID NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT activities_pk PRIMARY KEY (id),
    CONSTRAINT course_fk FOREIGN KEY (course_id) REFERENCES courses(id)
)
