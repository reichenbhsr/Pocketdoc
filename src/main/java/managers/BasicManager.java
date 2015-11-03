package managers;


import java.util.ArrayList;

/**
 * Created by nathic on 05.11.2014.
 */
public interface BasicManager<T> {

    /**
     * Die Methode wird gebraucht um ein Element T hinzuzufügen. Das Element wird dann in der Datenbank hinzugefügt und dann zurückgegeben.
     * <p>
     * Dabei soll die ID noch nicht gesetzt werden, diese wird in der Datenbank gesetzt.
     *
     * @return das Element mit Id
     */
    public T add();

    /**
     * Die Methode wird gebraucht um ein Element T zu verändern. Das Element wird dann in der Datenbank verändert und dann zurückgegeben.
     * <p>
     * Im Element T muss die Id definiert sein, damit die Methode korrekt funktioniert.
     * <p>
     * Fehlen Attribute im Element, dann werden diese von der Datenbank geholt, damit diese nicht gelöscht werden. Ausnahmen werden jeweils Beschrieben
     *
     * @param t das zu bearbeitende Element
     * @return das bearbeitete Element
     */
    public T update(T t);

    /**
     * Die Methode wird gebraucht um ein Element T zu holen. Das Element wird dann in der Datenbank geholt und dann zurückgegeben.
     * <p>
     * Die Elemente werden nach ihrer Id geholt.
     *
     * @param id Die ID des Elements
     * @return Das Element T
     */
    public T get(int id);

    /**
     * Die Methode wird gebraucht um alle Element der Klasse T zu holen. Die Elemente werden dann in der Datenbank geholt und dann zurückgegeben.
     *
     * @return Eine ArrayList mit allen Elementen
     */
    public ArrayList<T> getAll();

    /**
     * Die Methode wird gebraucht um ein Element T zu löschen. Das Element wird in der Datenbank gelöscht.
     * <p>
     * Die Elemente werden nach ihrer Id gelöscht.
     *
     * @param id Die Id des Elements
     */
    public void remove(int id);
}
