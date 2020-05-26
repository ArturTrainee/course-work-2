CREATE TABLE "event_attendee_relation"
(
    event_id BIGINT NOT NULL,
    attendee_id  BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES "event" (id),
    FOREIGN KEY (attendee_id) REFERENCES "attendee" (id)
);