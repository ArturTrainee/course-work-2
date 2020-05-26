CREATE TABLE "attendee"
(
    id           SERIAL PRIMARY KEY NOT NULL,
    display_name VARCHAR            NOT NULL,
    email        VARCHAR            NOT NULL,
    category     VARCHAR DEFAULT 'employee'
);