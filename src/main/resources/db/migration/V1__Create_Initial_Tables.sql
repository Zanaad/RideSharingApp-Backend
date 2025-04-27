-- Admin Table
CREATE TABLE admin
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Feedback Table
CREATE TABLE feedback
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    message      VARCHAR(255) NOT NULL,
    rating       INTEGER NOT NULL,
    user_id      INTEGER NOT NULL,
    ride_id      INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT feedback_user_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT feedback_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Discount Code Table
CREATE TABLE discount_code
(
    id               INTEGER NOT NULL AUTO_INCREMENT,
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    code             VARCHAR(255) NOT NULL UNIQUE,
    discount_percent INTEGER NOT NULL,
    expiration_date  DATETIME(6),
    PRIMARY KEY (id)
);

-- Trip Cancellation Table
CREATE TABLE trip_cancellation
(
    id             INTEGER NOT NULL AUTO_INCREMENT,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    cancellation_reason VARCHAR(255) NOT NULL,
    user_id        INTEGER NOT NULL,
    ride_id        INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT trip_cancellation_user_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT trip_cancellation_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Login History Table
CREATE TABLE login_history
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    ip_address  VARCHAR(255),
    user_agent  VARCHAR(255),
    user_id     INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT login_history_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Referral Table
CREATE TABLE referral
(
    id               INTEGER NOT NULL AUTO_INCREMENT,
    created_at       DATETIME(6),
    referred_user_id INTEGER NOT NULL,
    referrer_user_id INTEGER NOT NULL,
    referral_code    VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT referral_referred_fk FOREIGN KEY (referred_user_id) REFERENCES users (id),
    CONSTRAINT referral_referrer_fk FOREIGN KEY (referrer_user_id) REFERENCES users (id)
);

-- Chat Message Table
CREATE TABLE chat_message
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    message     VARCHAR(255) NOT NULL,
    sender_id   INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    ride_id     INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT chat_sender_fk FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT chat_receiver_fk FOREIGN KEY (receiver_id) REFERENCES users (id),
    CONSTRAINT chat_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Location Table
CREATE TABLE location
(
    id         INTEGER NOT NULL AUTO_INCREMENT,
    latitude   float(53) NOT NULL,
    longitude  float(53) NOT NULL,
    address    VARCHAR(255),
    PRIMARY KEY (id)
);

-- Ride History Table
CREATE TABLE ride_history
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    ride_id     INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ride_history_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id),
    CONSTRAINT ride_history_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Ride Priority Table
CREATE TABLE ride_priority
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    created_at  DATETIME(6),
    ride_id     INTEGER NOT NULL,
    priority_level ENUM('HIGH', 'MEDIUM', 'LOW') NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ride_priority_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);

-- Driver License Table
CREATE TABLE driver_license
(
    id              INTEGER NOT NULL AUTO_INCREMENT,
    created_at      DATETIME(6),
    driver_id       INTEGER NOT NULL,
    license_number  VARCHAR(255) NOT NULL UNIQUE,
    expiration_date DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT driver_license_driver_fk FOREIGN KEY (driver_id) REFERENCES drivers (id)
);
-- Trip Duration Table (or modification of the Rides Table)
CREATE TABLE trip_duration
(
    id              INTEGER NOT NULL AUTO_INCREMENT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    ride_id         INTEGER NOT NULL,
    start_time      DATETIME(6),
    end_time        DATETIME(6),
    duration_seconds INTEGER NOT NULL, -- Store the duration in seconds
    PRIMARY KEY (id),
    CONSTRAINT trip_duration_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);
