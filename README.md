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

## Installation

### macOS

1. **Install Java 21**
   ```bash
   brew install openjdk@21
   sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
   java -version
   ```

2. **Install PostgreSQL**
   ```bash
   brew install postgresql@16
   brew services start postgresql@16
   createdb chokusen
   ```

3. **Clone and configure the project**
   ```bash
   git clone <repo-url>
   cd chokusen
   ```

   Copy `src/main/resources/application-local.yml.example` to `application-local.yml` (gitignored) and fill in your values:
   ```yaml
   spring:
     datasource:
       password: <your-db-password>
   app:
     jwt:
       secret: <random-secret-at-least-32-characters>
   ```

   In any other environment, set the `DB_PASSWORD` and `JWT_SECRET` environment variables instead (`DB_URL` and `DB_USERNAME` are optional). The app refuses to start without them.

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   The API will be available at `http://localhost:8080`.

### Windows

1. **Install Java 21**

   Download and install the [Eclipse Temurin 21 JDK](https://adoptium.net/temurin/releases/?version=21) (or `winget install EclipseAdoptium.Temurin.21.JDK` in PowerShell), then verify:
   ```powershell
   java -version
   ```

2. **Install PostgreSQL**

   Download and install [PostgreSQL 16](https://www.postgresql.org/download/windows/), or via winget:
   ```powershell
   winget install PostgreSQL.PostgreSQL.16
   ```

   Then create the database (using the `psql` shell or pgAdmin):
   ```powershell
   psql -U postgres -c "CREATE DATABASE chokusen;"
   ```

3. **Clone and configure the project**
   ```powershell
   git clone <repo-url>
   cd chokusen
   ```

   Copy `src/main/resources/application-local.yml.example` to `application-local.yml` (gitignored) and fill in your values:
   ```yaml
   spring:
     datasource:
       password: <your-db-password>
   app:
     jwt:
       secret: <random-secret-at-least-32-characters>
   ```

   In any other environment, set the `DB_PASSWORD` and `JWT_SECRET` environment variables instead (`DB_URL` and `DB_USERNAME` are optional). The app refuses to start without them.

4. **Run the application**
   ```powershell
   .\gradlew.bat bootRun
   ```

   The API will be available at `http://localhost:8080`.

Hibernate will auto-create/update the schema on startup (`ddl-auto=update`) on either platform.

## API Documentation

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html` once the application is running, with the raw OpenAPI spec at `/v3/api-docs`. Both are public endpoints and don't require authentication.

## Authentication

All endpoints except `/api/v1/auth/**` require a valid JWT token.

On login, the token is set as an `httpOnly` cookie (`token`), so browser clients are authenticated automatically on subsequent requests — no extra header needed. Non-browser clients can alternatively send the token manually:
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

Sets an `httpOnly` cookie containing a JWT valid for 24 hours.

**Current user**
```http
GET /api/v1/me
```
Returns `{ userId, username, email }` for the authenticated user.

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
| GET | `/api/v1/workspaces/mine` | List the authenticated user's workspaces |
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

### Read state

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/workspaces/{workspaceId}/unread` | Unread message count per channel for the authenticated user |
| GET | `/api/v1/channels/{channelId}/reads` | When each user last read the channel |
| POST | `/api/v1/channels/{channelId}/read` | Mark the channel as read now; also broadcasts a receipt to `/topic/channel.{channelId}.reads` |

## Data Model

```
User ──< WorkspaceMember >── Workspace ──< Channel ──< Message
User ──< ChannelMember   >── Channel
User ──< ChannelReadState >── Channel   (last-read time per user and channel)
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

## Known Limitations

This is a basic first version. These gaps are known and deliberately deferred:

**Security**
- **No membership checks.** Any logged-in user can read any channel's messages, subscribe to any channel's live topic, send to any channel, invite people into any workspace, edit or delete any message, and list all users, workspaces and channels.
- **Logout does not revoke the token.** It only clears the cookie. The JWT stays valid until it expires (24 hours). The usual fix is a short-lived access token plus a rotating, server-stored refresh token, or server-side sessions.
- **CORS and cookie settings are development defaults.** CORS allows any origin with credentials, the cookie is not marked `Secure`, and Spring Security logs at `DEBUG`.

**Reliability**
- **WebSocket recovery is minimal.** After a reconnect, messages sent in the gap are not re-fetched. A failed send shows no error, and an unauthenticated connection retries every 3 seconds indefinitely.
- **The message broker is in memory.** Live updates only reach clients connected to the same backend instance, and nothing is retained for offline clients (they catch up from the database).

**Product**
- Inviting a workspace member adds them immediately. There is no pending invite or accept step.
- Invited members are not added to the workspace's channels automatically.
- A channel you have never opened counts its entire history as unread.
