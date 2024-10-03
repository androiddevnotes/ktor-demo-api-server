# Postgres

## Install

brew services start postgresql

createdb quotes_app_db

psql quotes_app_db

CREATE USER your_username WITH PASSWORD 'your_password';

CREATE USER adn_user WITH PASSWORD 'adn_password';

GRANT ALL PRIVILEGES ON DATABASE quotes_app_db TO adn_user;

\q

exit

## PgAdmin

brew install --cask pgadmin4

Open PgAdmin

In the "Create - Server" dialog that appears, fill in the following information:

General tab:

- Name: Give your server a name (e.g., "Local Quotes App")

Connection tab:

- Host name/address: localhost (if your PostgreSQL server is on the same machine)
- Port: 5432 (default PostgreSQL port)
- Maintenance database: postgres (default database)
- Username: adn_user (the user you created for your application)
- Password: adn_password (the password you set for adn_user)
