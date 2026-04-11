## Présentation

Viny Love Even't est une application permettant de gérer des événements.

Cette API backend permet de :
- gérer les utilisateurs
- créer et gérer les invités
- envoyer des invitations avec QR code
- contrôler les accès lors des événements

## Stack technique

- Backend : Java
- Framework : Spring Boot
- Base de données : PostgreSQL
- ORM : Spring Data JPA
- API : REST

## Architecture

Le projet suit une architecture en couches :

- Controller : gestion des requêtes HTTP
- Service : logique métier
- Repository : accès à la base de données
- Entity : représentation des données

## Entité User

Représente un utilisateur du système.

Champs :
- id : identifiant unique
- email : unique
- password : mot de passe
- firstName : prénom
- lastName : nom
- role : rôle (ADMIN, STAFF)
- createdAt : date de création

## Routes API

### GET /api/users
Récupérer tous les utilisateurs

### GET /api/users/{id}
Récupérer un utilisateur par id

### POST /api/users
Créer un utilisateur

### DELETE /api/users/{id}
Supprimer un utilisateur

## Exemple création utilisateur

POST /api/users

{
  "email": "admin@vinylove.com",
  "password": "123456",
  "firstName": "Aristote",
  "lastName": "Kasa",
  "role": "ADMIN"
}

## Installation

1. Cloner le projet
2. Configurer PostgreSQL
3. Modifier application.yml

## Lancer le projet

./mvnw spring-boot:run

## Base de données

PostgreSQL utilisé

Table principale :
- users