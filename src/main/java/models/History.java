package models;

import org.hibernate.annotations.Sort;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle histories
 *
 * @author Oliver Frischknecht
 */

public class History {

  private int id;

   private Set<Answer> answers;

    private Integer consecutiveQuestions;

    private int lastAnswerId;
    private Answer lastAnswer;

    public History() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Integer getConsecutiveQuestions() {
        return consecutiveQuestions;
    }
    public void setConsecutiveQuestions(Integer consecutiveQuestions) {
        this.consecutiveQuestions = consecutiveQuestions;
    }

    public void setLastAnswerId(int lastAnswerId){ this.lastAnswerId = lastAnswerId;}
    public Answer getLastAnswer() {

        if (lastAnswer == null)
            System.out.println("Last Answer not implemented!");

        return lastAnswer;
    }
    public void setLastAnswer(Answer lastAnswer) {
        this.lastAnswer = lastAnswer;
    }
}
