# Experiment Application

A Spring Boot-based application for managing tasks, comments, and attachments with user authentication and role-based access control.

## Features

- Task management (CRUD operations)
- Comment system for tasks
- File attachment handling
- User authentication and authorization
- Role-based access control (Admin, User roles)
- RESTful API endpoints

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: OpenAPI/Swagger
- **Testing**: JUnit 5, Mockito

## Project Structure

```
src/main/java/com/upskilling/experiment/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── entity/           # JPA entities
├── enums/            # Enumeration types
├── mapper/           # Object mappers (e.g., MapStruct)
├── repository/       # JPA repositories
└── service/          # Business logic and service implementations
```

## Prerequisites

- Java 17 or later
- Maven 3.6.3 or later
- PostgreSQL 13 or later

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd experiment
   ```

2. **Configure the database**
   - Create a PostgreSQL database
   - Update `application.properties` or `application.yml` with your database credentials

3. **Build the application**
   ```bash
   docker compose build
   ```

4. **Run the application**
   ```bash
   docker compose up
   ```

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication

The application uses JWT for authentication. Include the JWT token in the `Authorization` header for authenticated requests:

```
Authorization: Bearer <your-jwt-token>
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - Authenticate user and get JWT token
- `POST /api/auth/register` - Register a new user

### Users
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID

### Projects
- `GET /api/projects` - Get all projects
- `POST /api/projects` - Create a new project
- `GET /api/projects/{projectId}` - Get project by ID
- `PUT /api/projects/{projectId}` - Update a project
- `DELETE /api/projects/{projectId}` - Delete a project

### Task Lists
- `POST /api/tasklists` - Create a task list
- `GET /api/tasklists/{id}` - Get task list by ID
- `GET /api/tasklists/by-project/{projectId}` - Get task lists by project ID
- `PUT /api/tasklists/{id}` - Update a task list
- `DELETE /api/tasklists/{id}` - Delete a task list

### Tasks
- `GET /api/tasks` - Get all tasks
- `POST /api/tasks` - Create a new task
- `GET /api/tasks/{id}` - Get a task by ID
- `GET /api/tasks/by-list/{taskListId}` - Get tasks by task list ID
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

### Comments
- `GET /api/tasks/{taskId}/comments` - Get all comments for a task
- `POST /api/tasks/{taskId}/comments` - Add a comment to a task
- `PUT /api/comments/{id}` - Update a comment
- `DELETE /api/comments/{id}` - Delete a comment

### Attachments
- `GET /api/tasks/{taskId}/attachments` - Get all attachments for a task
- `POST /api/tasks/{taskId}/attachments` - Upload an attachment
- `GET /api/attachments/{id}` - Download an attachment
- `DELETE /api/attachments/{id}` - Delete an attachment

### Activity Logs
- `GET /api/logs/recent` - Get recent activity logs (Admin only)

## Testing

Run the test suite with:

```bash
mvn test
```

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication

The application uses JWT for authentication. Include the JWT token in the `Authorization` header for authenticated requests:

```
Authorization: Bearer <your-jwt-token>
```

### Required Headers
For endpoints that require authentication, include these headers:
```
Content-Type: application/json
Authorization: Bearer <your-jwt-token>
```

### Role-Based Access Control
- **ADMIN**: Full access to all endpoints
- **MANAGER**: Full access to all endpoints
- **MEMBER**: Limited access (cannot access user management or system logs)

