## Présentation

Viny Love Even't est une application permettant de gérer des événements.

Cette API backend permet de :
- gérer les utilisateurs
- créer et gérer les invités
- Créer et gérer des événements
- envoyer des invitations avec QR code
- contrôler les accès lors des événements
- Suivre les présences en temps réel

## Stack technique

- Backend : Java
- Framework : Spring Boot
- Base de données : PostgreSQL
- ORM : Spring Data JPA
- Sécurité : Spring Security + JWT (access token + refresh token)
- API : REST
- Encodage mot de passe : BCrypt

## Architecture

Le projet suit une architecture en couches :

- `controller` : gestion des requêtes HTTP
- `service` : logique métier
- `repository` : accès à la base de données
- `entity` : représentation des données
- `dto` : objets de transfert de données
- `security` : filtre JWT et authentification
- `config` : configuration Spring Security et JWT
- `exception` : gestion globale des erreurs

## Entités

### User
Représente un utilisateur du système (organisateur ou staff).

| Champ | Type | Description |
|---|---|---|
| id | Long | Identifiant unique |
| email | String | Email unique |
| password | String | Mot de passe (BCrypt) |
| firstName | String | Prénom |
| lastName | String | Nom |
| role | Role | ADMIN ou STAFF |
| createdAt | LocalDateTime | Date de création |

### Event
Représente un événement créé par un organisateur.

| Champ | Type | Description |
|---|---|---|
| id | Long | Identifiant unique |
| name | String | Nom de l'événement |
| description | String | Description |
| location | String | Lieu |
| eventDate | LocalDateTime | Date de l'événement |
| createdAt | LocalDateTime | Date de création |

### RefreshToken
Gère les tokens de rafraîchissement JWT.

| Champ | Type | Description |
|---|---|---|
| id | Long | Identifiant unique |
| token | String | Token unique |
| user | User | Utilisateur associé |
| expiryDate | LocalDateTime | Date d'expiration (7 jours) |
| revoked | boolean | Token révoqué ou non |
| createdAt | LocalDateTime | Date de création |

### Guest *(à venir)*
Représente un invité à un événement.

### Invitation *(à venir)*
Représente une invitation avec QR code unique.

### CheckIn *(à venir)*
Représente le scan d'entrée d'un invité.

## Routes API

### Utilisateurs

| Méthode | Route | Accès | Description |
|---|---|---|---|
| POST | `/api/users` | Public | Créer un utilisateur |
| POST | `/api/users/login` | Public | Connexion |
| POST | `/api/users/logout` | Public | Déconnexion |
| POST | `/api/users/refresh-token` | Public | Rafraîchir le token |
| GET | `/api/users/me` | Authentifié | Profil courant |
| PUT | `/api/users/me` | Authentifié | Modifier son profil |
| PUT | `/api/users/me/password` | Authentifié | Changer son mot de passe |
| GET | `/api/users` | ADMIN | Tous les utilisateurs |
| GET | `/api/users/{id}` | ADMIN | Utilisateur par id |
| PUT | `/api/users/{id}` | ADMIN | Modifier un utilisateur |
| DELETE | `/api/users/{id}` | ADMIN | Supprimer un utilisateur |

### Événements

| Méthode | Route | Accès | Description |
|---|---|---|---|
| POST | `/api/events` | ADMIN | Créer un événement |
| GET | `/api/events` | Authentifié | Tous les événements |
| GET | `/api/events/{id}` | Authentifié | Événement par id |
| DELETE | `/api/events/{id}` | ADMIN | Supprimer un événement |

### Invités *(à venir)*
| Méthode | Route | Accès | Description |
|---|---|---|---|
| POST | `/api/events/{id}/guests` | ADMIN | Ajouter un invité |
| GET | `/api/events/{id}/guests` | Authentifié | Liste des invités |
| DELETE | `/api/events/{id}/guests/{guestId}` | ADMIN | Supprimer un invité |

### Invitations *(à venir)*
| Méthode | Route | Accès | Description |
|---|---|---|---|
| POST | `/api/events/{id}/guests/{guestId}/invitation` | ADMIN | Générer une invitation |
| POST | `/api/invitations/send` | ADMIN | Envoyer par email |

### Check-in *(à venir)*
| Méthode | Route | Accès | Description |
|---|---|---|---|
| POST | `/api/checkin/scan` | STAFF | Scanner un QR code |
| GET | `/api/events/{id}/checkins` | Authentifié | Suivi des présences |

## Authentification

L'API utilise JWT avec deux tokens :
- **Access token** : durée de vie configurable (défaut 24h)
- **Refresh token** : durée de vie 7 jours, révocable

Toutes les requêtes protégées nécessitent le header :
AUTHORIZATION: Bearer <access_token>

## Exemples de requêtes

### Créer un utilisateur
```json
POST /api/users
{
  "email": "admin@vinylove.com",
  "password": "123456",
  "firstName": "Aristote",
  "lastName": "Kasa",
  "role": "ADMIN"
}
```

### Connexion
```json
POST /api/users/login
{
  "email": "admin@vinylove.com",
  "password": "123456"
}
```

### Rafraîchir le token
```json
POST /api/users/refresh-token
{
  "refreshToken": "uuid-du-refresh-token"
}
```

### Changer le mot de passe
```json
PUT /api/users/me/password
{
  "currentPassword": "ancienMotDePasse",
  "newPassword": "nouveauMotDePasse"
}
```

## Gestion des erreurs

| Code HTTP | Situation |
|---|---|
| 400 | Données invalides |
| 401 | Non authentifié / token invalide |
| 403 | Accès refusé (rôle insuffisant) |
| 404 | Ressource introuvable |
| 409 | Email déjà utilisé |


## Installation

1. Cloner le projet
2. Créer la base de données PostgreSQL :
```sql
CREATE DATABASE vinylove;
CREATE USER vinylove_user WITH PASSWORD '123Azerty';
GRANT ALL PRIVILEGES ON DATABASE vinylove TO vinylove_user;
```

3. Vérifier `application-local.yml` :
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vinylove
    username: vinylove_user
    password: 123Azerty
```

## Lancer le projet

```bash
./mvnw spring-boot:run
```

## Base de données

PostgreSQL — les tables sont générées automatiquement via Hibernate (`ddl-auto: update`).

Tables actuelles :
- `users`
- `events`
- `refresh_tokens`

Tables à venir :
- `guests`
- `invitations`
- `check_ins`