CREATE TABLE enrollments(
    id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    invitation_id UUID NOT NULL,
    student_id UUID NOT NULL,
    CONSTRAINT enrollments_pk PRIMARY KEY (id),
    CONSTRAINT invitations_fk FOREIGN KEY (invitation_id) REFERENCES invitations(id),
    CONSTRAINT students_fk FOREIGN KEY (student_id) REFERENCES users(id)
)
