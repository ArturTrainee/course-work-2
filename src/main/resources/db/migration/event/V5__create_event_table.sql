CREATE TABLE "event"
(
    "id"             SERIAL PRIMARY KEY,
    "calendar_id"    BIGINT    NOT NULL,
    "title"          VARCHAR   NOT NULL,
    "type"           VARCHAR   NOT NULL,
    "start_date"     TIMESTAMP NOT NULL,
    "end_date"       TIMESTAMP NOT NULL,
    "creation_date"  TIMESTAMP NOT NULL,
    "location"       VARCHAR   NOT NULL,
    "importance_lvl" INTEGER   NOT NULL,
    "description"    VARCHAR,
    "busy"           BOOLEAN   NOT NULL DEFAULT TRUE,
    "readonly"       BOOLEAN   NOT NULL DEFAULT FALSE,
    FOREIGN KEY (calendar_id) REFERENCES "calendar" (id)
)