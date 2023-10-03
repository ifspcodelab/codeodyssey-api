CREATE TABLE activities(
      id UUID NOT NULL,
      title VARCHAR(100) NOT NULL,
      description TEXT NOT NULL,
      start_date TIMESTAMP NOT NULL,
      end_date TIMESTAMP NOT NULL,
      initial_file BYTEA NOT NULL,
      solution_file BYTEA NOT NULL,
      test_file BYTEA NOT NULL,
      extension VARCHAR(15) NOT NULL,
      course_id UUID NOT NULL,
      CONSTRAINT activities_pk PRIMARY KEY (id),
      CONSTRAINT course_fk FOREIGN KEY (course_id) REFERENCES courses(id)
)