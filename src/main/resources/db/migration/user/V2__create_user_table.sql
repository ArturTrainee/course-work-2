CREATE TABLE "user"
(
    "id"           SERIAL PRIMARY KEY,
    "username"     VARCHAR      NOT NULL UNIQUE,
    "password"     VARCHAR      NOT NULL,
    "display_name" VARCHAR      NOT NULL,
    "email"        VARCHAR(255) NOT NULL UNIQUE,
    "roles"        VARCHAR      NOT NULL,
    "department"   VARCHAR      NOT NULL,
    "enabled"      BOOLEAN      NOT NULL DEFAULT TRUE
);