CREATE TABLE results (
    id UUID,
    name VARCHAR(255) NOT NULL,
    time DECIMAL NOT NULL,
    error TEXT NULL,
    activity_id UUID NOT NULL,
    CONSTRAINT results_pk PRIMARY KEY (id),
    CONSTRAINT activities_fk FOREIGN KEY (activity_id) REFERENCES activities (id)
);