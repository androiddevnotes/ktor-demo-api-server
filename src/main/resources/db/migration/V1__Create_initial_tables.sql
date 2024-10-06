CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS quotes (
    id SERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    author VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    category VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS dictionary_entries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    definition TEXT NOT NULL,
    examples TEXT,
    related_terms TEXT,
    tags TEXT,
    category VARCHAR(100) NOT NULL,
    languages TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);