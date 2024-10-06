# Deployment Guide

## Heroku Deployment

1. Create a Heroku app:
   ```
   heroku create your-app-name
   ```

2. Set up Heroku Postgres:
   ```
   heroku addons:create heroku-postgresql:hobby-dev
   ```

3. Set environment variables:
   ```
   heroku config:set JWT_SECRET=your_jwt_secret
   heroku config:set JWT_ISSUER=your_jwt_issuer
   heroku config:set JWT_AUDIENCE=your_jwt_audience
   heroku config:set UPLOAD_DIR=/app/uploads
   ```

4. Deploy the application:
   ```
   git push heroku main
   ```

5. Run database migrations:
   ```
   heroku run ./gradlew flywayMigrate
   ```

## Troubleshooting

- Check Heroku logs: `heroku logs --tail`
- Ensure `Procfile` is correctly configured
- Verify database connection string in `application.yaml`

## Scaling

To scale your application on Heroku: