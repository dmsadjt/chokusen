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

   Open `src/main/resources/application.properties` and set your values:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/chokusen
   spring.datasource.username=<your-db-user>
   spring.datasource.password=<your-db-password>
   jwt.secret=<your-secret-key>
   ```

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

   Open `src/main/resources/application.properties` and set your values:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/chokusen
   spring.datasource.username=<your-db-user>
   spring.datasource.password=<your-db-password>
   jwt.secret=<your-secret-key>
   ```

4. **Run the application**
   ```powershell
   .\gradlew.bat bootRun
   ```

   The API will be available at `http://localhost:8080`.

Hibernate will auto-create/update the schema on startup (`ddl-auto=update`) on either platform.

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
