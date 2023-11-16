CREATE TABLE resolutions(
    id UUID NOT NULL,
    activity_id UUID NOT NULL,
    student_id UUID NOT NULL,
    submit_date TIMESTAMP NOT NULL,
    resolution_file TEXT NOT NULL,
    status VARCHAR NOT NULL,
    CONSTRAINT resolutions_pk PRIMARY KEY (id),
    CONSTRAINT activities_fk FOREIGN KEY (activity_id) REFERENCES activities(id),
    CONSTRAINT users_fk FOREIGN KEY (student_id) REFERENCES users(id)
)
