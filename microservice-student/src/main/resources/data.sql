INSERT INTO students (id, name, last_name, email, course_id) VALUES (1, 'Alice', 'Johnson', 'alice.johnson@email.com', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), last_name = VALUES(last_name), email = VALUES(email), course_id = VALUES(course_id);

INSERT INTO students (id, name, last_name, email, course_id) VALUES (2, 'Bob', 'Williams', 'bob.williams@email.com', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), last_name = VALUES(last_name), email = VALUES(email), course_id = VALUES(course_id);

INSERT INTO students (id, name, last_name, email, course_id) VALUES (3, 'Charlie', 'Brown', 'charlie.brown@email.com', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name), last_name = VALUES(last_name), email = VALUES(email), course_id = VALUES(course_id);

INSERT INTO students (id, name, last_name, email, course_id) VALUES (4, 'Diana', 'Taylor', 'diana.taylor@email.com', 3)
ON DUPLICATE KEY UPDATE name = VALUES(name), last_name = VALUES(last_name), email = VALUES(email), course_id = VALUES(course_id);

INSERT INTO students (id, name, last_name, email, course_id) VALUES (5, 'Ethan', 'Anderson', 'ethan.anderson@email.com', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name), last_name = VALUES(last_name), email = VALUES(email), course_id = VALUES(course_id);
