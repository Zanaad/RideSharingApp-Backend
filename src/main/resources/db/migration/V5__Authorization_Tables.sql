CREATE TABLE role (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);


CREATE TABLE permission (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    verb ENUM('GET', 'POST', 'PUT', 'DELETE') NOT NULL,
    resource VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE role_permission (
    id INT NOT NULL AUTO_INCREMENT,
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (role_id, permission_id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);


ALTER TABLE users
    ADD COLUMN role_id INT,
    ADD CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES role(id);

-- Remove user_type (replaced by role_id)
ALTER TABLE users
    DROP COLUMN user_type;

-- Seed initial roles
INSERT INTO role (name) VALUES
    ('ADMIN'),
    ('DRIVER'),
    ('PASSENGER');
