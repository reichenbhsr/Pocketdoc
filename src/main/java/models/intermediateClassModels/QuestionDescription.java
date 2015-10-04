package models.intermediateClassModels;

import database.mappers.LanguageConnector;
import database.mappers.QuestionConnector;
import models.Language;
import models.Question;

import javax.persistence.*;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle question_descriptions
 *
 * @author Oliver Frischknecht
 */

public class QuestionDescription {

    private int id;

    private int questionId;
    private Question question;

    private int languageId;
    private Language language;

    private String description;

    public QuestionDescription() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public Question getQuestion() {

        if (question == null){
            QuestionConnector con = new QuestionConnector();
            question = con.read(questionId);
        }

        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setLanguageId(int languageId) { this.languageId = languageId; }
    public Language getLanguage() {

        if (language == null){
            LanguageConnector con = new LanguageConnector();
            language = con.read(languageId);
        }

        return language;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}


