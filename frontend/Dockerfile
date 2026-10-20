#  instaleer les dépendances et compil Angular
FROM node:22-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm ci --silent

# On copie lecode source
COPY . .

# fichiers statiques
RUN npm run build

# serveur pour servir les fichiers
FROM nginx:alpine

# on copie les fichier produits
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html


COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

