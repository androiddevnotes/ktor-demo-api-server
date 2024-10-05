# Deploying to Heroku

This guide will walk you through the process of deploying your Ktor application to Heroku.

## Prerequisites

1. [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) installed on your machine
2. A Heroku account
3. Git installed on your machine

## Steps

1. **Login to Heroku CLI**

   Open a terminal and run:
   ```
   heroku login
   ```

2. **Create a Heroku app**

   In the root directory of your project, run:
   ```
   heroku create your-app-name
   ```
   Replace `your-app-name` with a unique name for your application.

3. **Configure Heroku Postgres**

   Add a Postgres database to your Heroku app:
   ```
   heroku addons:create heroku-postgresql:hobby-dev
   ```

4. **Set environment variables**

   Set the necessary environment variables:
   ```
   heroku config:set JWT_SECRET=your_jwt_secret
   heroku config:set JWT_ISSUER=your_jwt_issuer
   heroku config:set JWT_AUDIENCE=your_jwt_audience
   heroku config:set UPLOAD_DIR=/app/uploads
   ```
   Replace the values with your actual JWT configuration and desired upload directory.

5. **Deploy the application**

   Commit any changes you've made and push to Heroku:
   ```
   git add .
   git commit -m "Prepare for Heroku deployment"
   git push heroku main
   ```

6. **Run database migrations (if needed)**

   If you need to run any database migrations, you can do so using:
   ```
   heroku run ./gradlew flywayMigrate
   ```
   Note: This step assumes you're using Flyway for database migrations. Adjust as necessary for your setup.

7. **Open the application**

   Once the deployment is complete, you can open your app in a web browser:
   ```
   heroku open
   ```

## Troubleshooting

- If you encounter boot timeout issues, check the Heroku logs:
  ```
  heroku logs --tail
  ```

- If the application is taking too long to start, consider optimizing your initialization process, especially database operations.

- Ensure that your `Procfile` is correctly configured and in the root directory of your project:
  ```
  web: ./build/install/com.example.ktor-sample/bin/com.example.ktor-sample
  ```
  Replace `com.example.ktor-sample` with your actual project name if different.

- If the issue persists, you might need to increase the dyno's memory. You can do this by changing to a larger dyno type:
  ```
  heroku ps:resize web=standard-1x
  ```
  Note that this will increase your Heroku bill.

- Make sure your `application.conf` or `application.yaml` file is correctly reading environment variables for database configuration and other sensitive information.

## Scaling

To scale your application, you can use:
