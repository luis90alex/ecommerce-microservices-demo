INSERT INTO courses (id, name, teacher) VALUES (1, 'Java Fundamentals', 'John Smith')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (2, 'Spring Boot Microservices', 'Jane Doe')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (3, 'React for Beginners', 'Carlos López')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (4, 'Advanced SQL', 'Maria García')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (5, 'Docker and Kubernetes', 'Peter Johnson')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (6, 'Python Data Science', 'Laura Martínez')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (7, 'Angular Complete Guide', 'David Wilson')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (8, 'AWS Cloud Practitioner', 'Sofia Hernández')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (9, 'Clean Code & Design Patterns', 'Robert Brown')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);

INSERT INTO courses (id, name, teacher) VALUES (10, 'DevOps Fundamentals', 'Emma Davis')
ON DUPLICATE KEY UPDATE name = VALUES(name), teacher = VALUES(teacher);
