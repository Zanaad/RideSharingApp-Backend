CREATE TABLE Companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES Companies(company_id),
    FOREIGN KEY (role_id) REFERENCES UserRoles(role_id)
);

CREATE TABLE Drivers (
    driver_id INT PRIMARY KEY,
    license_number VARCHAR(255),
    car_model VARCHAR(255),
    car_plate VARCHAR(20),
    rating DECIMAL(3, 2),
    FOREIGN KEY (driver_id) REFERENCES Users(user_id)
);

CREATE TABLE Passengers (
    passenger_id INT PRIMARY KEY,
    rating DECIMAL(3, 2),
    FOREIGN KEY (passenger_id) REFERENCES Users(user_id)
);

CREATE TABLE Trips (
    trip_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    driver_id INT,
    departure_location VARCHAR(255) NOT NULL,
    destination_location VARCHAR(255) NOT NULL,
    departure_time DATETIME NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id),
    FOREIGN KEY (company_id) REFERENCES Companies(company_id),
    FOREIGN KEY (status_id) REFERENCES TripStatuses(status_id)
);

CREATE TABLE Bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    trip_id INT,
    passenger_id INT,
    seats_booked INT NOT NULL,
    status_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES Trips(trip_id),
    FOREIGN KEY (passenger_id) REFERENCES Passengers(passenger_id),
    FOREIGN KEY (status_id) REFERENCES BookingStatuses(status_id)
);

CREATE TABLE Reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    trip_id INT,
    reviewer_id INT,
    reviewed_id INT,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES Trips(trip_id),
    FOREIGN KEY (reviewer_id) REFERENCES Users(user_id),
    FOREIGN KEY (reviewed_id) REFERENCES Users(user_id)
);

CREATE TABLE Payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    payment_status_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id),
    FOREIGN KEY (payment_status_id) REFERENCES PaymentStatuses(status_id)
);

CREATE TABLE UserRoles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO UserRoles (role_name) VALUES ('passenger'), ('driver');

CREATE TABLE TripStatuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO TripStatuses (status_name) VALUES ('pending'), ('confirmed'), ('completed'), ('canceled');

CREATE TABLE BookingStatuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO BookingStatuses (status_name) VALUES ('pending'), ('confirmed'), ('canceled');

CREATE TABLE PaymentStatuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO PaymentStatuses (status_name) VALUES ('pending'), ('completed'), ('failed');