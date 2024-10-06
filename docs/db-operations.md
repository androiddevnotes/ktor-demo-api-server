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

## Manual Database Schema Management (With vs Without Flyway)

Let's compare the process of renaming the `dictionaryentries` table to `dictionary_entries` with and without Flyway:

### With Flyway

1. **Create a Migration Script**:
   Create a new file in `src/main/resources/db/migration/` named `V3__Rename_dictionaryentries_table.sql`:
   ```sql
   ALTER TABLE dictionaryentries RENAME TO dictionary_entries;
   ```

2. **Run the Application**:
   Simply start your application:
   ```
   ./gradlew run
   ```
   Flyway will automatically detect and apply the new migration.

3. **Update Application Code**:
   Update the Kotlin code to reflect the new table name (e.g., in `DictionaryEntry.kt`).

4. **Version Control**:
   Commit the new migration script and code changes to your version control system.

### Without Flyway

1. **Create a SQL Script**:
   Create a SQL script with the rename operation:
   ```sql
   ALTER TABLE dictionaryentries RENAME TO dictionary_entries;
   ```

2. **Manual Execution**:
   - Connect to each database environment (development, staging, production) using a tool like psql:
     ```
     psql -U adn_user -d quotes_app_db
     ```
   - Manually run the SQL command in each environment:
     ```sql
     ALTER TABLE dictionaryentries RENAME TO dictionary_entries;
     ```

3. **Update Application Code**:
   Update the Kotlin code to reflect the new table name.

4. **Version Control**:
   - Manually track which scripts have been run on which environments.
   - Consider keeping a log file or spreadsheet of applied changes.
   - Add comments to the scripts indicating when and where they were applied.

5. **Coordination**:
   Ensure all team members are aware of the change and update their local databases.

6. **Deployment Process**:
   Include database update steps in the deployment process for each environment.

7. **Rollback Plan**:
   Prepare and document how to revert the change if needed:
   ```sql
   ALTER TABLE dictionary_entries RENAME TO dictionaryentries;
   ```

### Comparison

- **Consistency**: Flyway ensures the change is applied consistently across all environments. Without Flyway, there's a risk of inconsistency if the manual process is not followed exactly in each environment.
- **Automation**: With Flyway, the migration is applied automatically when the application starts. Without Flyway, each environment requires manual intervention.
- **Version Control**: Flyway provides built-in versioning for database changes. Without Flyway, version control for database changes must be managed manually.
- **Rollback**: Flyway can automatically handle rollbacks to previous versions. Without Flyway, rollbacks must be planned and executed manually.
- **Team Coordination**: Flyway reduces the need for team coordination around database changes. Without Flyway, more communication is required to ensure all team members apply changes correctly.

Using Flyway significantly simplifies the process of managing database schema changes, reducing the potential for errors and inconsistencies across different environments.
