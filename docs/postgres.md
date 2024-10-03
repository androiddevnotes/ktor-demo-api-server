brew services start postgresql

createdb quotes_app_db

psql quotes_app_db

CREATE USER your_username WITH PASSWORD 'your_password';

CREATE USER adn_user WITH PASSWORD 'adn_password';

GRANT ALL PRIVILEGES ON DATABASE quotes_app_db TO adn_user;

\q

exit

