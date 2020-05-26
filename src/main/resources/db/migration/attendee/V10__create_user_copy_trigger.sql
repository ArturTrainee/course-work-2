CREATE TRIGGER trig_copy
    AFTER INSERT
    ON "user"
    FOR EACH ROW
EXECUTE PROCEDURE function_copy();