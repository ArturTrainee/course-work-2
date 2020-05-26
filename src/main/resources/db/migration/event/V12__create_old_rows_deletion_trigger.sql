CREATE TRIGGER trigger_delete_old_rows
    AFTER INSERT
    ON "event"
    FOR EACH ROW
EXECUTE PROCEDURE delete_old_rows();