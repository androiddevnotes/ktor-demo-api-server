# Database Migrations with Flyway

## Overview

Our application uses Flyway for managing database migrations. This ensures that our database schema evolves consistently across all environments.

## Key Points

- Migration scripts are located in `src/main/resources/db/migration/`
- Naming convention: `V{VersionNumber}__{Description}.sql`
- Migrations are applied automatically when the application starts

## Recent Migrations

### V1__Create_initial_tables.sql
- Created tables for users, quotes, dictionary entries, and API keys

### V2__Add_category_to_quotes.sql
- Added a `category` column to the `quotes` table

### V3__Add_resources_to_dictionary_entries.sql
- Added a `resources` column to the `dictionary_entries` table

## Running Migrations Manually

If needed, you can run migrations manually using:
