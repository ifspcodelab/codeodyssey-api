CREATE TABLE activities(
    id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR NOT NULL,
    course_id UUID NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    initial_file VARCHAR NOT NULL,
    solution_file VARCHAR NOT NULL,
    test_file VARCHAR NOT NULL,
    extension VARCHAR NOT NULL,
    CONSTRAINT activities_pk PRIMARY KEY (id),
    CONSTRAINT course_fk FOREIGN KEY (course_id) REFERENCES courses(id)
)
