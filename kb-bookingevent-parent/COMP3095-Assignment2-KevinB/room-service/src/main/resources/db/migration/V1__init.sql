CREATE TABLE room (
    id BIGSERIAL PRIMARY KEY,
    room_name VARCHAR(255) NOT NULL,
    capacity INT,
    features VARCHAR(255),
    available BOOLEAN DEFAULT TRUE
);

