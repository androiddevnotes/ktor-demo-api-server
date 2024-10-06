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
- Image upload functionality for quotes
- Categorization of quotes

### 2. Dictionary Management

- CRUD operations for dictionary entries
- Search functionality for dictionary entries
- Support for examples, related terms, tags, and resources for each entry

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

## API Routes

The application exposes the following main API routes:

1. Authentication routes (`/api/v1/register`, `/api/v1/login`)
2. Quote routes (`/api/v1/quotes`)
3. Dictionary routes (`/api/v1/dictionary`)
4. API key generation route (`/api/v1/api-key`)

## Configuration

The application uses a combination of environment variables and a `application.yaml` file for configuration. Key configurations include:

- Database connection details
- JWT settings
- Upload directory for images

## Deployment

The application is designed to be deployed on platforms like Heroku, with support for automatic database migrations using Flyway.

## Development Workflow

1. Use Gradle for building and running the application
2. Database migrations are managed through Flyway and can be found in `src/main/resources/db/migration`
3. The `DatabaseSeeder` class can be used to populate the database with initial data

For more detailed information on specific components, please refer to the individual documentation files in the `docs` directory.