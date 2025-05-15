-- Feedback Table
CREATE TABLE feedback
(
    id         INTEGER      NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    message    VARCHAR(255) NOT NULL,
    rating     INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    ride_id    INTEGER      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT feedback_user_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT feedback_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Discount Code Table
CREATE TABLE discount_code
(
    id               INTEGER      NOT NULL AUTO_INCREMENT,
    created_at       DATETIME(6),
    code             VARCHAR(255) NOT NULL UNIQUE,
    discount_percent INTEGER      NOT NULL,
    expiration_date  DATETIME(6),
    PRIMARY KEY (id)
);

-- Trip Cancellation Table
CREATE TABLE trip_cancellation
(
    id                  INTEGER      NOT NULL AUTO_INCREMENT,
    created_at          DATETIME(6),
    cancellation_reason VARCHAR(255) NOT NULL,
    user_id             INTEGER      NOT NULL,
    ride_id             INTEGER      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT trip_cancellation_user_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT trip_cancellation_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Login History Table
CREATE TABLE login_history
(
    id         INTEGER NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    user_id    INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT login_history_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
-- Chat Message Table
CREATE TABLE chat_message
(
    id          INTEGER      NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    message     VARCHAR(255) NOT NULL,
    sender_id   INTEGER      NOT NULL,
    receiver_id INTEGER      NOT NULL,
    ride_id     INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT chat_sender_fk FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT chat_receiver_fk FOREIGN KEY (receiver_id) REFERENCES users (id),
    CONSTRAINT chat_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Location Table
CREATE TABLE location
(
    id        INTEGER   NOT NULL AUTO_INCREMENT,
    latitude  float(53) NOT NULL,
    longitude float(53) NOT NULL,
    address   VARCHAR(255),
    PRIMARY KEY (id)
);

-- Driver License Table
CREATE TABLE driver_license
(
    id              INTEGER      NOT NULL AUTO_INCREMENT,
    created_at      DATETIME(6),
    driver_id       INTEGER      NOT NULL,
    license_number  VARCHAR(255) NOT NULL UNIQUE,
    expiration_date DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT driver_license_driver_fk FOREIGN KEY (driver_id) REFERENCES drivers (id)
);


ALTER TABLE drivers DROP COLUMN license_Number;
ALTER TABLE rides ADD COLUMN pickup_time DATETIME(6);
