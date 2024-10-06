# Database Operations with Flyway

## Introduction

This guide explains how to use Flyway for managing database migrations in our Ktor project. Flyway helps us version control our database schema and apply changes consistently across different environments.

## How Flyway Works in Our Project

1. **Configuration**: Flyway is configured in `DatabaseConfig.kt`.
2. **Migration Scripts**: SQL scripts for schema changes are stored in `src/main/resources/db/migration/`.
3. **Automatic Execution**: Migrations run automatically when the application starts.

## Creating and Applying Migrations

### Step 1: Create a Migration Script

1. Navigate to `src/main/resources/db/migration/`.
2. Create a new SQL file with the naming convention:
   `V{VersionNumber}__{Description}.sql`
   
   Example: `V2__Add_email_to_users.sql`

3. Write your SQL statements in this file:

   ```sql
   ALTER TABLE users ADD COLUMN email VARCHAR(255);
   ```

### Step 2: Run the Application

1. Simply start your application:
   ```
   ./gradlew run
   ```
2. Flyway will automatically detect and apply new migrations.

## Best Practices

1. **Version Control**: Always commit migration scripts to your version control system.
2. **One Change per Migration**: Each migration script should handle one schema change.
3. **Idempotency**: Write migrations that can be run multiple times without causing errors.
4. **Testing**: Test migrations in a non-production environment before applying to production.

## Troubleshooting

- If you encounter errors related to existing tables, you may need to baseline your database:
  ```kotlin
  flyway.baseline()
  ```
  Add this line before `flyway.migrate()` in `DatabaseConfig.kt` if needed.

- Check Flyway's schema history table (`flyway_schema_history`) to see which migrations have been applied.

## Manual Database Operations

While Flyway handles most database operations automatically, you might occasionally need to perform manual operations:

1. **Connecting to the Database**:
   ```
   psql -U adn_user -d quotes_app_db
   ```

2. **Viewing Applied Migrations**:
   ```sql
   SELECT * FROM flyway_schema_history ORDER BY installed_rank;
   ```

3. **Manually Applying Migrations** (if needed):
   ```
   ./gradlew flywayMigrate
   ```

Remember, manual operations should be used sparingly, as Flyway is designed to handle migrations automatically and consistently.

## Recent Changes

### Adding Resources to Dictionary Entries

We've added a new field `resources` to the `dictionary_entries` table. This field stores URLs or references to additional resources related to each dictionary entry.

To add this new field, we created a new migration script:

1. Created `V2__Add_resources_to_dictionary_entries.sql` in `src/main/resources/db/migration/`:

   ```sql
   ALTER TABLE dictionary_entries ADD COLUMN resources TEXT;
   ```

2. Updated the `DictionaryEntry` data class and related repository operations to include the new field.

3. When adding new dictionary entries or updating existing ones, you can now include resource links:

   ```kotlin
   val newEntry = DictionaryEntry(
       name = "API",
       definition = "Application Programming Interface",
       // ... other fields ...
       resources = listOf(
           "https://en.wikipedia.org/wiki/API",
           "https://www.mulesoft.com/resources/api/what-is-an-api"
       )
   )
   ```

4. When retrieving dictionary entries, the `resources` field will now be available:

   ```kotlin
   val entry = dictionaryRepository.getById(1)
   println(entry.resources) // List of resource URLs
   ```

Remember to update your application logic, API endpoints, and any related frontend components to make use of this new field.

## Conclusion

By using Flyway, we ensure that our database schema evolves consistently with our application code. Always create a new migration script for each database change, and let Flyway handle the application of these changes across all environments.
