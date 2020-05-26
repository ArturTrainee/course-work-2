CREATE OR REPLACE FUNCTION function_copy() RETURNS TRIGGER AS
$BODY$
BEGIN
    INSERT INTO "attendee"(id, display_name, email)
    VALUES (new.id, new.display_name, new.email);

    RETURN new;
END;
$BODY$
    language plpgsql;