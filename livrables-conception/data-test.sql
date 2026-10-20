INSERT INTO users (user_name, email, password) VALUES ('valentincorger', 'valentin.corger@gmail.com', 'password');

INSERT INTO users (user_name, email, password) VALUES ('Jane Doe', 'jane.doe@gmail.com', 'password');

INSERT INTO projects (name, description, start_date) VALUES ('Project 1', 'Description 1', '2022-01-01');

INSERT INTO roles (name) VALUES ('ADMIN'), ('MEMBER'), ('OBSERVER');

INSERT INTO user_project_roles (project_id, user_id, role_id) VALUES (1, 1, 1), (1, 2, 2);

INSERT INTO tasks (name, description, due_date, priority, project_id, status) VALUES ('Task 1', 'Description 1', '2022-01-01', 'HIGH', 1, 'TODO'), ('Task 2', 'Description 2', '2022-01-02', 'MEDIUM', 1, 'IN_PROGRESS'), ('Task 3', 'Description 3', '2022-01-03', 'LOW', 1, 'DONE');

INSERT INTO user_tasks (task_id, user_id) VALUES (1, 1), (1, 2);