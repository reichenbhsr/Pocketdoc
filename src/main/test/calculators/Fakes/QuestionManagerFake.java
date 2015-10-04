package calculators.Fakes;

import database.mappers.DatabaseMapper;
import managers.QuestionManager;
import models.Question;
import models.User;

import java.util.ArrayList;

/**
 * Created by nbourqui on 07.11.2014.
 */
public class QuestionManagerFake extends QuestionManager {

    private ArrayList<Question> list;

    public QuestionManagerFake(ArrayList<Question> list) {
        this.list = new ArrayList<Question>(list);
    }

    @Override
    public ArrayList<Question> getAll() {
        return list;
    }

    @Override
    public Question getAndFetch(Question question) {
        return question;
    }
}
