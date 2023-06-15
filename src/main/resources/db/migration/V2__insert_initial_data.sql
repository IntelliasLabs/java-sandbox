-- Insert statements for the Users table
INSERT INTO users (username, password, email, phone_number)
VALUES ('user1', 'password1', 'user1@example.com', '1234567890');

INSERT INTO users (username, password, email, phone_number)
VALUES ('user2', 'password2', 'user2@example.com', '9876543210');

-- Insert statements for the ParkingSpaces table
INSERT INTO parking_spaces (name, location)
VALUES ('Space1', 'Location1');
INSERT INTO parking_spaces (name, location)
VALUES ('Space2', 'Location1');
INSERT INTO parking_spaces (name, location)
VALUES ('Space3', 'Location1');
INSERT INTO parking_spaces (name, location)
VALUES ('Space4', 'Location1');

INSERT INTO parking_spaces (name, location)
VALUES ('Space1', 'Location2');
INSERT INTO parking_spaces (name, location)
VALUES ('Space2', 'Location2');
INSERT INTO parking_spaces (name, location)
VALUES ('Space3', 'Location2');
INSERT INTO parking_spaces (name, location)
VALUES ('Space4', 'Location2');

INSERT INTO parking_spaces (name, location)
VALUES ('Space1', 'Location3');
INSERT INTO parking_spaces (name, location)
VALUES ('Space2', 'Location3');
INSERT INTO parking_spaces (name, location)
VALUES ('Space3', 'Location3');
INSERT INTO parking_spaces (name, location)
VALUES ('Space4', 'Location3');

-- Insert statements for the Bookings table
INSERT INTO bookings (user_id, space_id, start_time, end_time)
VALUES (1, 1, '2022-06-30 10:00:00', '2022-06-30 12:00:00');
INSERT INTO bookings (user_id, space_id, start_time, end_time)
VALUES (2, 2, '2022-07-01 09:00:00', '2022-07-01 11:00:00');
INSERT INTO bookings (user_id, space_id, start_time, end_time)
VALUES (1, 2, '2022-07-02 14:00:00', '2022-07-02 16:00:00');
INSERT INTO bookings (user_id, space_id, start_time, end_time)
VALUES (2, 3, '2022-07-03 11:00:00', '2022-07-03 13:00:00');
INSERT INTO bookings (user_id, space_id, start_time, end_time)
VALUES (1, 4, '2022-07-04 09:00:00', '2022-07-04 10:30:00');
