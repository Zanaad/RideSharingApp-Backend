CREATE TABLE password_reset_token
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    token       VARCHAR(255) NOT NULL,
    user_id     INTEGER UNIQUE,
    expiry_date TIMESTAMP,

    CONSTRAINT fk_user_password_reset FOREIGN KEY (user_id) REFERENCES users (id)
);
ALTER TABLE users DROP INDEX email;
ALTER TABLE companies ADD UNIQUE (domain);
ALTER TABLE payments
    MODIFY payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED', 'CANCELLED');

ALTER TABLE rides
DROP
COLUMN completed_at,
DROP
COLUMN pickup_time,
DROP
COLUMN estimated_fare;

ALTER TABLE rides
    MODIFY COLUMN status ENUM ('ACCEPTED','CANCELLED','COMPLETED','ONGOING','REQUESTED') NOT NULL;

ALTER TABLE rides
    ADD COLUMN pickup_lat DOUBLE NOT NULL,
    ADD COLUMN pickup_lng DOUBLE NOT NULL,
    ADD COLUMN dropoff_lat DOUBLE NOT NULL,
    ADD COLUMN dropoff_lng DOUBLE NOT NULL;
