package models;

import models.intermediateClassModels.DiagnosisDescription;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle languages
 *
 * @author Oliver Frischknecht
 */

public class Language {

    private int id;

    private String name;

    private String code;

    public Language() {

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

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

}
