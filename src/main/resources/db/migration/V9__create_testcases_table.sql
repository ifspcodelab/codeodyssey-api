CREATE TABLE testcases (
    id UUID,
    test_name VARCHAR(255) NOT NULL,
    success BOOLEAN NOT NULL,
    info TEXT NULL,
    time DECIMAL NOT NULL,
    result_id UUID NOT NULL,
    CONSTRAINT testcases_pk PRIMARY KEY (id),
    CONSTRAINT results_fk FOREIGN KEY (result_id) REFERENCES results (id)
);