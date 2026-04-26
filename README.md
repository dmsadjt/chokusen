# Chokusen

A real-time collaborative messaging API built with Spring Boot. Organize conversations into workspaces and channels, with JWT-based authentication.

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Build | Gradle |

## Prerequisites

- Java 21+
- PostgreSQL running on `localhost:5432`
- A database named `chokusen`

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd chokusen
   ```

2. **Configure the application**

   Open `src/main/resources/application.properties` and set your values:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/chokusen
   spring.datasource.username=<your-db-user>
   spring.datasource.password=<your-db-password>
   jwt.secret=<your-secret-key>
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   The API will be available at `http://localhost:8080`.

   Hibernate will auto-create/update the schema on startup (`ddl-auto=update`).

## Authentication

All endpoints except `/api/v1/auth/**` require a valid JWT token.

Include the token in every request header:
```
Authorization: Bearer <token>
```

**Register**
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "alice",
  "email": "alice@example.com",
  "password": "secret"
}
```

**Login**
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "secret"
}
```

Returns a JWT token valid for 24 hours.

## API Reference

### Users

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/users` | List all users |
| GET | `/api/v1/users/{userId}` | Get a user |
| PUT | `/api/v1/users/{userId}` | Update a user |
| DELETE | `/api/v1/users/{userId}` | Delete a user |

### Workspaces

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/workspaces` | List all workspaces |
| POST | `/api/v1/workspaces` | Create a workspace |
| GET | `/api/v1/workspaces/{workspaceId}` | Get a workspace |
| PUT | `/api/v1/workspaces/{workspaceId}` | Update a workspace |
| DELETE | `/api/v1/workspaces/{workspaceId}` | Delete a workspace |
| GET | `/api/v1/workspaces/{workspaceId}/members` | List workspace members |
| POST | `/api/v1/workspaces/{workspaceId}/members` | Add a member (with role) |
| DELETE | `/api/v1/workspaces/{workspaceId}/members/{userId}` | Remove a member |

### Channels

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/channels` | List all channels |
| POST | `/api/v1/channels` | Create a channel |
| GET | `/api/v1/channels/{channelId}` | Get a channel |
| PUT | `/api/v1/channels/{channelId}` | Update a channel |
| DELETE | `/api/v1/channels/{channelId}` | Delete a channel |
| GET | `/api/v1/workspaces/{workspaceId}/channels` | List channels in a workspace |
| GET | `/api/v1/channels/{channelId}/members` | List channel members |
| POST | `/api/v1/channels/{channelId}/members` | Join a channel |
| DELETE | `/api/v1/channels/{channelId}/members/{userId}` | Leave a channel |

### Messages

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/channels/{channelId}/messages` | Get messages in a channel |
| PUT | `/api/v1/channels/{channelId}/messages/{messageId}` | Edit a message |
| DELETE | `/api/v1/channels/{channelId}/messages/{messageId}` | Delete a message |

## Data Model

```
User ──< WorkspaceMember >── Workspace ──< Channel ──< Message
User ──< ChannelMember   >── Channel
```

- All primary keys are UUIDs.
- `WorkspaceMember` tracks the user's role in a workspace.
- Channels can be public or private (`isPrivate`).

## Project Structure

```
src/main/java/com/dmsadjt/chokusen/
├── controller/      # REST controllers
├── service/         # Business logic interfaces + implementations
├── entity/          # JPA entities
├── repository/      # Spring Data repositories
├── dto/             # Request/response DTOs
└── security/        # JWT utilities, filters, Spring Security config
```
