-- PMT - Project Management Tool
-- Script de génération de la base de données PostgreSQL

CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    user_name  VARCHAR(255),
    email      VARCHAR(255) UNIQUE,
    password   VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE projects (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255),
    description TEXT,
    start_date  DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE roles (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE "userProjectRoles" (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users(id),
    project_id BIGINT REFERENCES projects(id),
    role_id    BIGINT REFERENCES roles(id)
);

CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,
    project_id  BIGINT REFERENCES projects(id),
    name        VARCHAR(255),
    description TEXT,
    due_date    DATE,
    priority    VARCHAR(50),
    status      VARCHAR(50),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE task_history (
    id         BIGSERIAL PRIMARY KEY,
    task_id    BIGINT REFERENCES tasks(id),
    field      VARCHAR(100),
    old_value  TEXT,
    new_value  TEXT,
    changed_at TIMESTAMP
);

CREATE TABLE "userTasks" (
    task_id BIGINT REFERENCES tasks(id),
    user_id BIGINT REFERENCES users(id),
    PRIMARY KEY (task_id, user_id)
);

-- Données de test

INSERT INTO users (user_name, email, password, created_at) VALUES
    ('Valentin Corger', 'valentin.corger@gmail.com', 'password', NOW()),
    ('Alice Martin',    'alice.martin@example.com',  'password', NOW()),
    ('Bob Dupont',      'bob.dupont@example.com',    'password', NOW());

INSERT INTO projects (name, description, start_date, created_at, updated_at) VALUES
    ('PMT',         'Application de gestion de projet',  '2024-01-01', NOW(), NOW()),
    ('Site Vitrine', 'Refonte du site institutionnel',   '2024-02-15', NOW(), NOW());

INSERT INTO roles (name, created_at) VALUES
    ('ADMIN',    NOW()),
    ('MEMBER',   NOW()),
    ('OBSERVER', NOW());

INSERT INTO "userProjectRoles" (user_id, project_id, role_id) VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (1, 2, 1);

INSERT INTO tasks (project_id, name, description, due_date, priority, status, created_at, updated_at) VALUES
    (1, 'Conception BDD',       'Modéliser les entités et les relations', '2024-01-10', 'HIGH',   'DONE',        NOW(), NOW()),
    (1, 'API REST Spring Boot', 'Implémenter les controllers et services', '2024-01-20', 'HIGH',   'DONE',        NOW(), NOW()),
    (1, 'Interface Angular',    'Développer le kanban et les formulaires', '2024-01-30', 'MEDIUM', 'IN_PROGRESS', NOW(), NOW()),
    (1, 'Tests et couverture',  'Atteindre 60% de couverture de code',    '2024-02-05', 'MEDIUM', 'TODO',        NOW(), NOW()),
    (2, 'Maquettes Figma',      'Créer les maquettes de la page accueil', '2024-03-01', 'LOW',    'TODO',        NOW(), NOW());

INSERT INTO "userTasks" (task_id, user_id) VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 2),
    (4, 3);
