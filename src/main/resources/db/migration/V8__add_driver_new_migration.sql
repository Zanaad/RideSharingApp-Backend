--new driver permission


INSERT INTO permission (id, name, verb, resource) VALUES
(82, 'VIEW_MY_RATINGS', 'GET', '/api/ratings/my'),
(83, 'VIEW_MY_RIDES', 'GET', '/api/rides/my'),
(84, 'UPDATE_RIDE_STATUS', 'PUT', '/api/rides/{id}/status');

INSERT INTO role_permission (role_id, permission_id) VALUES

(2, 82),
(2, 83),
(2, 84);