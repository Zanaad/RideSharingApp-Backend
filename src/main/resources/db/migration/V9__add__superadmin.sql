INSERT INTO users(email, first_name, last_name, password, phone_number, company_id, role_id)
values ('superadmin@gmail.com', 'zana', 'ademi', '$2a$10$21MSD3jLcdVY48x27/NDYexFj36wRhotMF/ldKwjBzlW6Fyao6OtW',
        '09876543', null, 4);

INSERT INTO permission (name, verb, resource)
VALUES ('Get Payments By RideId', 'GET', '/api/payments/ride/{rideId}'),
       ('Get My Payments', 'GET', '/api/payments/my-payments'),
       ('Complete the Payment', 'POST', '/api/payments/create-intent'),
       ('Cancel a Ride', 'PUT', '/api/rides/cancel/{id}');

INSERT INTO role_permission (role_id, permission_id)
VALUES (3, 85),
       (3, 86),
       (3, 87),
       (3, 88);