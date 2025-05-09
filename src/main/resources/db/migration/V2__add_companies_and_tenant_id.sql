CREATE TABLE companies
(
    id            INTEGER      NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL UNIQUE,
    address       VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(255),
    domain        VARCHAR(255),
    status        ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at    DATETIME(6),
    PRIMARY KEY (id)
);


ALTER TABLE users
    ADD COLUMN company_id INTEGER;
ALTER TABLE users
    ADD CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE drivers
    ADD COLUMN company_id INTEGER;
ALTER TABLE drivers
    ADD CONSTRAINT fk_drivers_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE rides
    ADD COLUMN company_id INTEGER;
ALTER TABLE rides
    ADD CONSTRAINT fk_rides_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE ride_tracking
    ADD COLUMN company_id INTEGER;
ALTER TABLE rides
    ADD CONSTRAINT fk_ride_tracking_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE vehicle
    ADD COLUMN company_id INTEGER;
ALTER TABLE vehicle
    ADD CONSTRAINT fk_vehicle_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE payments
    ADD COLUMN company_id INTEGER;
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE ratings
    ADD COLUMN company_id INTEGER;
ALTER TABLE ratings
    ADD CONSTRAINT fk_ratings_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE notifications
    ADD COLUMN company_id INTEGER;
ALTER TABLE notifications
    ADD CONSTRAINT fk_notifications_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE users
    MODIFY COLUMN user_type ENUM('DRIVER', 'PASSENGER', 'ADMIN');
