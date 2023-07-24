CREATE TABLE users (
      id UUID PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      email VARCHAR(255) UNIQUE NOT NULL,
      password VARCHAR(100) NOT NULL,
      role VARCHAR(100) NOT NULL,
      created_at TIMESTAMP NOT NULL,
      is_validated BOOLEAN NOT NULL,
      token VARCHAR(100) NOT NULL
);