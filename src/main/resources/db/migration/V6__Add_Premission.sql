
INSERT INTO permission (id, name, verb, resource) VALUES

-- VEHICLE PERMISSIONS
(9, 'READ_VEHICLE_BY_ID', 'GET', '/api/vehicles/{id}'),
(10, 'UPDATE_VEHICLE', 'PUT', '/api/vehicles/{id}'),
(11, 'DELETE_VEHICLE', 'DELETE', '/api/vehicles/{id}'),
(12, 'READ_VEHICLES', 'GET', '/api/vehicles'),
(13, 'CREATE_VEHICLE', 'POST', '/api/vehicles'),
(14, 'READ_VEHICLE_BY_DRIVER_ID', 'GET', '/api/vehicles/driver/{driverId}');

-- USER PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(15, 'READ_USER_BY_ID', 'GET', '/api/users/{id}'),
(16, 'UPDATE_USER_BY_ID', 'PUT', '/api/users/{id}'),
(17, 'DELETE_USER_BY_ID', 'DELETE', '/api/users/{id}'),
(18, 'CHANGE_USER_PASSWORD', 'PUT', '/api/users/{id}/change-password'),
(19, 'READ_ALL_USERS', 'GET', '/api/users'),
(20, 'READ_MY_USER', 'GET', '/api/users/me');


-- PAYMENT PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(21, 'READ_PAYMENT_BY_ID', 'GET', '/api/payments/{id}'),
(22, 'UPDATE_PAYMENT', 'PUT', '/api/payments/{id}'),
(23, 'DELETE_PAYMENT', 'DELETE', '/api/payments/{id}'),
(24, 'READ_ALL_PAYMENTS', 'GET', '/api/payments'),
(25, 'CREATE_PAYMENT', 'POST', '/api/payments'),
(26, 'READ_PAYMENT_BY_USER_ID', 'GET', '/api/payments/user/{userId}'),
(27, 'READ_PAYMENT_BY_STATUS', 'GET', '/api/payments/status/{status}'),
(28, 'READ_PAYMENT_BY_RIDE_ID', 'GET', '/api/payments/ride/{rideId}'),
(29, 'READ_PAYMENT_COMPANY', 'GET', '/api/payments/company');


-- NOTIFICATION PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(30, 'MARK_NOTIFICATION_AS_READ', 'PUT', '/api/notifications/{id}/read'),
(31, 'READ_ALL_NOTIFICATIONS', 'GET', '/api/notifications'),
(32, 'CREATE_NOTIFICATION', 'POST', '/api/notifications'),
(33, 'READ_NOTIFICATION_BY_ID', 'GET', '/api/notifications/{id}'),
(34, 'DELETE_NOTIFICATION', 'DELETE', '/api/notifications/{id}');


-- COMPANY PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(35, 'READ_COMPANY_BY_ID', 'GET', '/api/companies/{id}'),
(36, 'UPDATE_COMPANY', 'PUT', '/api/companies/{id}'),
(37, 'READ_ALL_COMPANIES', 'GET', '/api/companies'),
(38, 'CREATE_COMPANY', 'POST', '/api/companies'),
(39, 'READ_COMPANY_BY_DOMAIN', 'GET', '/api/companies/domain/{domain}');


-- RIDE PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(40, 'READ_ALL_RIDES', 'GET', '/api/rides'),
(41, 'CREATE_RIDE', 'POST', '/api/rides'),
(42, 'READ_RIDE_BY_ID', 'GET', '/api/rides/{id}'),
(43, 'DELETE_RIDE', 'DELETE', '/api/rides/{id}'),
(44, 'READ_RIDES_BY_PASSENGER', 'GET', '/api/rides/passenger/{passengerId}'),
(45, 'READ_COMPANY_RIDES', 'GET', '/api/rides/company'),
(46, 'READ_COMPANY_RIDES_BY_STATUS', 'GET', '/api/rides/company/status/{status}'),
(47, 'READ_COMPANY_RIDES_BY_PASSENGER', 'GET', '/api/rides/company/passenger/{passengerId}'),
(48, 'READ_COMPANY_RIDES_BY_DRIVER', 'GET', '/api/rides/company/driver/{driverId}');



-- RATING PERMISSIONS
INSERT INTO permission (id, name, verb, resource) VALUES
(53, 'READ_ALL_RATINGS', 'GET', '/api/ratings'),
(54, 'CREATE_RATING', 'POST', '/api/ratings'),
(55, 'READ_RATING_BY_ID', 'GET', '/api/ratings/{id}'),
(56, 'DELETE_RATING_BY_ID', 'DELETE', '/api/ratings/{id}'),
(57, 'READ_RATINGS_BY_RIDE_ID', 'GET', '/api/ratings/ride/{rideId}'),
(58, 'READ_RATINGS_BY_DRIVER_ID', 'GET', '/api/ratings/driver/{driverId}'),
(59, 'READ_COMPANY_RATINGS', 'GET', '/api/ratings/company');





-- DRIVER DASHBOARD PERMISSION
INSERT INTO permission (id, name, verb, resource) VALUES
(62, 'ACCESS_DRIVER_DASHBOARD', 'GET', '/api/dashboard');


-- Role Controller
INSERT INTO permission (id, name, verb, resource) VALUES
(63, 'READ_ROLE_BY_ID', 'GET', '/api/roles/{id}'),
(64, 'UPDATE_ROLE_BY_ID', 'PUT', '/api/roles/{id}'),
(65, 'DELETE_ROLE_BY_ID', 'DELETE', '/api/roles/{id}'),
(66, 'READ_ALL_ROLES', 'GET', '/api/roles'),
(67, 'CREATE_ROLE', 'POST', '/api/roles');





-- Permsion-controller

INSERT INTO permission (id, name, verb, resource) VALUES
(68, 'READ_PERMISSION_BY_ID', 'GET', '/api/permissions/{id}'),
(69, 'UPDATE_PERMISSION_BY_ID', 'PUT', '/api/permissions/{id}'),
(70, 'DELETE_PERMISSION_BY_ID', 'DELETE', '/api/permissions/{id}'),
(71, 'READ_ALL_PERMISSIONS', 'GET', '/api/permissions'),
(72, 'CREATE_PERMISSION', 'POST', '/api/permissions');


-- role perimsion-controller

INSERT INTO permission (id, name, verb, resource) VALUES
(73, 'READ_ALL_ROLE_PERMISSIONS', 'GET', '/api/role-permissions'),
(74, 'CREATE_ROLE_PERMISSION', 'POST', '/api/role-permissions'),
(75, 'READ_ROLE_PERMISSIONS_BY_ROLE', 'GET', '/api/role-permissions/role/{roleId}'),
(76, 'DELETE_ROLE_PERMISSION_BY_ID', 'DELETE', '/api/role-permissions/{id}');

-- Driver Permission
INSERT INTO permission (id, name, verb, resource) VALUES
(77, 'READ_DRIVER_BY_ID', 'GET', '/api/drivers/{id}'),
(78, 'UPDATE_DRIVER_BY_ID', 'PUT', '/api/drivers/{id}'),
(79, 'DELETE_DRIVER_BY_ID', 'DELETE', '/api/drivers/{id}'),
(80, 'READ_ALL_DRIVERS', 'GET', '/api/drivers'),
(81, 'CREATE_DRIVER', 'POST', '/api/drivers');




-- Vehicle perms
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14);

INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 9),
(2, 12),
(2, 14);


-- User PERMISSION
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20);

INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 18),
(2, 20);

INSERT INTO role_permission (role_id, permission_id) VALUES
(3,16),
(3, 18),
(3, 20);



-- PAYMENT PERMISSIONS
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 21), (1, 22), (1, 23), (1, 24), (1, 25),
(1, 26), (1, 27), (1, 28), (1, 29);

INSERT INTO role_permission (role_id, permission_id) VALUES

(3, 25),
(3, 26),
(3,27),
(3,28);



-- NOTIFICATIONS
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 30), (1, 31), (1, 32), (1, 33), (1, 34);

-- NOTIFICATIONS
INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 30), (2, 33), (2, 34);

-- NOTIFICATIONS
INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 30), (3, 33),(3,34);



-- COMPANIES
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 35), (1, 36), (1, 37), (1, 38), (1, 39);

-- COMPANY
INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 35), (2, 39);

-- COMPANY
INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 35), (3, 39);



-- RATING PERMISSIONS

INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 53), (1, 54), (1, 55), (1, 56), (1, 57), (1, 58), (1, 59);


INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 57), (2, 58);


INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 54),(3,56), (3, 55), (3, 57);



INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 62);

INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 62);



-- Per RIDE
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 40),
(1, 41),
(1, 42),
(1, 43),
(1, 44),
(1, 45),
(1, 46),
(1, 47),
(1, 48);


INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 45),
(2, 46),
(2, 48);

INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 41),
(3, 42),
(3, 44),
(3, 47);


-- Permision
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 68),
(1, 69),
(1, 70),
(1, 71),
(1, 72);

INSERT INTO role (name) VALUE
    ('SUPERADMIN');


-- Driver
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 77),
(1, 78),
(1, 79),
(1, 80),
(1, 81);


INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 77),
(2, 78),
(2, 80);



INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 77),
(3, 80);

