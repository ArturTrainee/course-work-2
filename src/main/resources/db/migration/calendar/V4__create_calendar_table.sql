create TABLE "calendar"
(
    "id"         SERIAL PRIMARY KEY,
    "name"       VARCHAR     NOT NULL UNIQUE,
    "text_color" VARCHAR(20) NOT NULL,
    "bg_color"   VARCHAR(20) NOT NULL
);