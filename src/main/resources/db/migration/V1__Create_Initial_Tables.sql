create table users
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    created_at   DATETIME(6),
    email        VARCHAR(255) NOT NULL UNIQUE,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    user_type    ENUM ('DRIVER','PASSENGER'),
    primary key (id)
);
create table drivers
(
    id             INTEGER      NOT NULL AUTO_INCREMENT,
    license_number VARCHAR(255) NOT NULL UNIQUE,
    rating         float(23)    NOT NULL,
    status         ENUM ('AVAILABLE','BUSY','OFFLINE'),
    user_id        INTEGER UNIQUE,
    primary key (id),
    CONSTRAINT driver_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
create table rides
(
    id                INTEGER   NOT NULL AUTO_INCREMENT,
    completed_at      DATETIME(6),
    created_at        DATETIME(6),
    distance          float(53) NOT NULL,
    drop_off_location VARCHAR(255),
    estimated_fare    float(53) NOT NULL,
    fare              float(53) NOT NULL,
    pickup_location   VARCHAR(255),
    status            ENUM ('ACCEPTED','CANCELLED','COMPLETED','ONGOING','REQUESTED'),
    driver_id         INTEGER,
    passenger_id      INTEGER   NOT NULL,
    primary key (id),
    CONSTRAINT ride_driver_fk FOREIGN KEY (driver_id) REFERENCES drivers (id),
    CONSTRAINT ride_user_fk FOREIGN KEY (passenger_id) REFERENCES users (id)
);
create table ride_tracking
(
    id        INTEGER   NOT NULL AUTO_INCREMENT,
    latitude  float(53) NOT NULL,
    longitude float(53) NOT NULL,
    timestamp DATETIME(6),
    ride_id   INTEGER   NOT NULL,
    primary key (id),
    CONSTRAINT ride_tracking_id_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);
create table vehicle
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    brand        VARCHAR(255),
    color        VARCHAR(255),
    model        VARCHAR(255),
    plate_number VARCHAR(255) NOT NULL UNIQUE,
    vehicle_type ENUM ('MOTORBIKE','SEDAN','SUV','VAN'),
    driver_id    INTEGER UNIQUE,
    primary key (id),
    CONSTRAINT vehicle_driver_fk FOREIGN KEY (driver_id) REFERENCES drivers (id)
);

create table payments
(
    id             INTEGER   NOT NULL AUTO_INCREMENT,
    amount         float(53) NOT NULL,
    created_at     DATETIME(6),
    payment_method ENUM ('CARD','CASH','WALLET'),
    payment_status tinyint,
    ride_id        INTEGER UNIQUE,
    user_id        INTEGER   NOT NULL,
    primary key (id),
    CONSTRAINT payment_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id),
    CONSTRAINT payment_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
create table ratings
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    created_at   DATETIME(6),
    rating       INTEGER NOT NULL,
    review       VARCHAR(255),
    driver_id    INTEGER NOT NULL,
    passenger_id INTEGER NOT NULL,
    ride_id      INTEGER UNIQUE,
    primary key (id),
    CONSTRAINT rating_driver_fk FOREIGN KEY (driver_id) REFERENCES drivers (id),
    CONSTRAINT rating_passenger_fk FOREIGN KEY (passenger_id) REFERENCES users (id),
    CONSTRAINT rating_ride_fk FOREIGN KEY (ride_id) REFERENCES rides (id)
);
create table notifications
(
    id         INTEGER NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    is_read    bit     NOT NULL,
    message    VARCHAR(255),
    type       ENUM ('GENERAL','PAYMENT','RIDE_UPDATE'),
    user_id    INTEGER NOT NULL,
    primary key (id),
    CONSTRAINT notifications_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);