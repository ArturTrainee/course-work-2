CREATE FUNCTION delete_old_rows() RETURNS TRIGGER AS
$BODY$
BEGIN
    DELETE FROM "event_attendee_relation" r USING "event" e
    WHERE e."end_date" < now() AND e.id = r.event_id;

    DELETE FROM "event"
    WHERE "event"."end_date" < now();

    RETURN NULL;
END;
$BODY$
    language plpgsql;