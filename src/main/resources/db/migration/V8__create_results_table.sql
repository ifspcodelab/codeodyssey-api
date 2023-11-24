CREATE TABLE results (
    id UUID,
    name VARCHAR(255) NOT NULL,
    time DECIMAL NOT NULL,
    error TEXT NULL,
    resolution_id UUID NOT NULL,
    CONSTRAINT results_pk PRIMARY KEY (id),
    CONSTRAINT resolutions_fk FOREIGN KEY (resolution_id) REFERENCES resolutions (id)
);