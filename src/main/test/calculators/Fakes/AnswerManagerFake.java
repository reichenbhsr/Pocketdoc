package calculators.Fakes;

import managers.AnswerManager;
import managers.HistoryManager;
import models.Answer;
import models.History;

/**
 * Created by nbourqui on 17.12.2014.
 */
public class AnswerManagerFake extends AnswerManager {


    @Override
    public Answer getAndFetch(Answer answer) {
        return answer;
    }
}
