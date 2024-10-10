# Application Overview

This document provides an overview of the Ktor-based API application for managing quotes and a dictionary.

## Core Components

1. **Quotes Management**
2. **Dictionary Management**
3. **User Authentication**
4. **API Key Management**

## Technology Stack

- **Backend Framework**: Ktor
- **Database**: PostgreSQL
- **ORM**: Exposed
- **Authentication**: JWT and API Keys
- **Database Migration**: Flyway
- **Serialization**: Kotlinx Serialization
- **Logging**: SLF4J with Logback

## Application Structure

The application follows a modular structure with the following main packages:

- `com.example.apikey`: API key management
- `com.example.common`: Common utilities and configurations
- `com.example.dictionary`: Dictionary-related functionality
- `com.example.quotes`: Quote management
- `com.example.user`: User management and authentication

## Key Features

### 1. Quotes Management

- CRUD operations for quotes
- Pagination support for retrieving quotes
- Search functionality for quotes
- Categorization of quotes
- Image upload functionality for quotes

### 2. Dictionary Management

- CRUD operations for dictionary entries
- Pagination support for retrieving dictionary entries
- Search functionality for dictionary entries
- Categorization of dictionary entries
- Support for examples, related terms, tags, languages, and additional resources

### 3. User Authentication

- User registration and login
- JWT-based authentication
- Role-based access control (USER and ADMIN roles)

### 4. API Key Management

- Generation of API keys for users
- API key-based authentication for certain routes

## Database Schema

The application uses the following main tables:

1. `users`: Stores user information
2. `quotes`: Stores quote data
3. `dictionary_entries`: Stores dictionary entries
4. `api_keys`: Stores API keys associated with users

## API Routes and Usage

The application exposes the following main API routes:

### Authentication Routes

1. Register a new user:
   - `POST /api/v1/register`
   - Body: `{ "username": "newuser", "password": "password123" }`

2. Login and get JWT token:
   - `POST /api/v1/login`
   - Body: `{ "username": "existinguser", "password": "password123" }`
   - Response includes a JWT token to be used for authenticated requests

### API Key Generation

3. Generate an API key (requires authentication):
   - `POST /api/v1/api-key`
   - Header: `Authorization: Bearer <jwt_token>`
   - Response includes the generated API key

### Quote Routes

4. Get all quotes (paginated):
   - `GET /api/v1/quotes?page=1&pageSize=10`

5. Get a specific quote:
   - `GET /api/v1/quotes/{id}`

6. Create a new quote (authenticated):
   - `POST /api/v1/quotes`
   - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`
   - Body: `{ "content": "Quote text", "author": "Author Name", "category": "Category" }`

7. Update a quote (authenticated):
   - `PUT /api/v1/quotes/{id}`
   - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`
   - Body: `{ "content": "Updated quote text", "author": "Updated Author", "category": "Updated Category" }`

8. Delete a quote (authenticated):
   - `DELETE /api/v1/quotes/{id}`
   - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`

### Dictionary Routes

9. Get all dictionary entries (paginated):
   - `GET /api/v1/dictionary?page=1&pageSize=10`

10. Get a specific dictionary entry:
    - `GET /api/v1/dictionary/{id}`

11. Search dictionary entries:
    - `GET /api/v1/dictionary/search?q=searchterm`

12. Create a new dictionary entry (authenticated):
    - `POST /api/v1/dictionary`
    - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`
    - Body: `{ "name": "Term", "definition": "Definition", "examples": ["Example 1"], "relatedTerms": ["Related 1"], "tags": ["Tag1"], "category": "Category", "languages": ["English"] }`

13. Update a dictionary entry (authenticated):
    - `PUT /api/v1/dictionary/{id}`
    - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`
    - Body: (same structure as create, with updated fields)

14. Delete a dictionary entry (authenticated):
    - `DELETE /api/v1/dictionary/{id}`
    - Header: `Authorization: Bearer <jwt_token>` or `X-API-Key: <api_key>`

## Pagination

Both the Quotes and Dictionary components support pagination:

- Clients can specify `page` and `pageSize` parameters in GET requests
- Responses include metadata about the current page, total entries, and total pages

## Image Upload and Storage

- Images for quotes are uploaded using multipart form data
- Images are stored in the `uploads` directory (configurable via `UPLOAD_DIR` environment variable)
- The application serves images statically from the `/images` route

## Configuration

The application uses a combination of environment variables and an `application.yaml` file for configuration. Key configurations include:

- Database connection details
- JWT settings
- Upload directory for images

Environment variables can be set in a `.env` file for local development.

## Database Migrations with Flyway

- Flyway is used for managing database schema changes
- Migration scripts are located in `src/main/resources/db/migration`
- Migrations are automatically applied when the application starts
- New migrations can be added by creating SQL files in the migration directory

## Deployment

The application is designed to be deployed on platforms like Heroku:

1. Create a Heroku app: `heroku create your-app-name`
2. Set up Heroku Postgres: `heroku addons:create heroku-postgresql:hobby-dev`
3. Set environment variables:
   ```
   heroku config:set JWT_SECRET=your_jwt_secret
   heroku config:set JWT_ISSUER=your_jwt_issuer
   heroku config:set JWT_AUDIENCE=your_jwt_audience
   heroku config:set UPLOAD_DIR=/app/uploads
   ```
4. Deploy the application: `git push heroku main`
5. Run database migrations: `heroku run ./gradlew flywayMigrate`

## Development Workflow

1. Use Gradle for building and running the application: `./gradlew run`
2. Database migrations are managed through Flyway and can be found in `src/main/resources/db/migration`
3. The `DatabaseSeeder` class can be used to populate the database with initial data
4. Use the provided curl commands in `docs/curl.md` for testing API endpoints

## API Key Generation and Usage

1. Register a user or log in to get a JWT token
2. Use the JWT token to make a POST request to `/api/v1/api-key` to generate an API key
3. Use the API key in subsequent requests by including it in the `X-API-Key` header

## Recent Updates

- Added three new dictionary entries: Algorithm, API, and Machine Learning.
- Created a new Flyway migration script (V5) to insert these entries into the database.
- Updated the process for populating the dictionary using the API and user's API key.

For more detailed information on specific components, please refer to the individual documentation files in the `docs` directory.