package database.mappers;

/**
 * Diese Exception wird geworfen, wenn ein Fehler beim Mappen auftaucht
 *
 * @author Oliver Frischknecht
 */
public class DatabaseMapperException extends Exception {
    public DatabaseMapperException() {
        super();
    }

    public DatabaseMapperException(String message) {
        super(message);
    }
}
