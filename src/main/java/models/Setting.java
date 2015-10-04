package models;


import javax.persistence.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle settings
 *
 * @author Oliver Frischknecht
 */

public class Setting {

    private int id;

    private String name;

    private int value;

    public Setting() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
