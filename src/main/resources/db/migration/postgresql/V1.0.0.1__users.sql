CREATE TABLE copsboot_user (
                               id_value UUID NOT NULL,
                               email VARCHAR(255),
                               password VARCHAR(255),
                               PRIMARY KEY (id_value)
);

CREATE TABLE user_roles (
                            user_id_value UUID NOT NULL,
                            roles VARCHAR(255)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id_value)
            REFERENCES copsboot_user (id_value);