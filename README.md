# Project Management Tool - PMT

Application de gestion de projet avec un kanban par statut des tâches.

Stack : Angular 21 (frontend), Spring Boot 4 (backend), PostgreSQL.

---

## Prérequis

- Docker et Docker Compose installés
- Un compte Docker Hub (pour la CI/CD)
- Java 21 et Maven (pour le dev local)
- Node 22 et npm (pour le dev local)

---

## Lancer l'application avec Docker

Cloner le repo :

```bash
git clone <url-du-repo>
cd "Projet PMT fin UE"
```

Lancer les conteneurs :

```bash
docker compose up --build
```

- Frontend accessible sur : http://localhost:80
- Backend accessible sur : http://localhost:8080
- Base de données PostgreSQL sur le port 5432

Pour stopper :

```bash
docker compose down
```

---

## Lancer en local (sans Docker)

**Backend**

Configurer la base de données dans `backend/iscod/src/main/resources/application.properties` puis :

```bash
cd backend/iscod
./mvnw spring-boot:run
```

**Frontend**

```bash
cd frontend
npm install
npm start
```

L'application est accessible sur http://localhost:4200

---

## Variables d'environement pour la CI/CD

Ajouter ces secrets dans Github (Settings > Secrets > Actions) :

- `DOCKERHUB_USERNAME` : votre nom d'utilisateur Docker Hub
- `DOCKERHUB_TOKEN` : access token généré depuis hub.docker.com

La pipeline se déclenche automatiquement à chaque push sur master. Elle execute les tests, build les images Docker et les push sur Docker Hub.

---

## Structure du projet

```
.
├── frontend/        Angular 21
├── backend/iscod/   Spring Boot 4
└── .github/
    └── workflows/   Pipeline CI/CD
```

---

## Comptes de test

Email : valentin.corger@gmail.com  
Mot de passe : password
