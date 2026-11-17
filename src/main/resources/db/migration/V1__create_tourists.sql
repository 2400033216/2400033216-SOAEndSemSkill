CREATE TABLE tourists (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    contact_number VARCHAR(25),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tourist_visited_destinations (
    tourist_id BIGINT NOT NULL REFERENCES tourists(id) ON DELETE CASCADE,
    destination VARCHAR(160) NOT NULL
);

CREATE INDEX idx_tourists_email ON tourists (LOWER(email));

INSERT INTO tourists (full_name, email, contact_number)
VALUES
    ('Aarav Sharma', 'aarav@example.com', '+91 9876543210'),
    ('Ananya Reddy', 'ananya@example.com', '+91 9123456780');

INSERT INTO tourist_visited_destinations (tourist_id, destination)
VALUES
    (1, 'Charminar'),
    (1, 'Golconda Fort'),
    (2, 'Taj Mahal');
