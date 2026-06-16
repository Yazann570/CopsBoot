CREATE TABLE report (
    id UUID NOT NULL,
    reporter_id UUID,
    date_time   TIMESTAMP WITH TIME ZONE,
    description VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE report
    ADD CONSTRAINT fk_report_reporter
    FOREIGN KEY (reporter_id)
    REFERENCES copsboot_user;