CREATE TABLE IF NOT EXISTS Users(
    id Serial Primary Key,
    login Varchar(25) Unique Not Null,
    password VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS Locations (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL ,
    userId INTEGER NOT NULL ,
    latitude DOUBLE PRECISION NOT NULL ,
    longitude DOUBLE PRECISION NOT NULL ,
    FOREIGN KEY (userId) references Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Sessions (
    id UUID PRIMARY KEY ,
    userId INTEGER UNIQUE NOT NULL ,
    expiresAt TIMESTAMP NOT NULL ,
    FOREIGN KEY (userId) references Users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_location_userId ON Locations(userId);
