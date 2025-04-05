-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Create clubs table if it doesn't exist
CREATE TABLE IF NOT EXISTS clubs (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200)
);

-- Create tournaments table if it doesn't exist
CREATE TABLE IF NOT EXISTS tournaments (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    location VARCHAR(200) NOT NULL,
    name VARCHAR(100) NOT NULL,
    number_of_mats INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Create participants table if it doesn't exist
CREATE TABLE IF NOT EXISTS participants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR(20) NOT NULL,
    skill_level VARCHAR(20) NOT NULL,
    club_id INTEGER REFERENCES clubs(id)
);

-- Create participant_tournaments join table if it doesn't exist
CREATE TABLE IF NOT EXISTS participant_tournaments (
    participant_id INTEGER REFERENCES participants(id),
    tournament_id INTEGER REFERENCES tournaments(id),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (participant_id, tournament_id)
); 