CREATE TABLE report (
                        id_value UUID NOT NULL,
                        reporter_id UUID NOT NULL,
                        date_time TIMESTAMP WITH TIME ZONE,
                        description VARCHAR(255),
                        PRIMARY KEY (id_value)
);

ALTER TABLE report
    ADD CONSTRAINT fk_report_reporter
        FOREIGN KEY (reporter_id)
            REFERENCES copsboot_user (id_value);