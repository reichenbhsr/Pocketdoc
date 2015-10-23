package managers;

import calculators.DiagnosisCalculator;
import database.mappers.DiagnosisConnector;
import database.mappers.intermediateClassMappers.AnswerToDiagnosisScoreDistributionConnector;
import database.mappers.intermediateClassMappers.DiagnosisDescriptionConnector;
import database.mappers.intermediateClassMappers.PerfectDiagnosisDiagnosesToAnswersConnector;
import managers.intermediateClassManagers.DiagnosisDescriptionManager;
import models.Answer;
import models.Diagnosis;
import models.Language;
import models.User;
import models.intermediateClassModels.DiagnosisDescription;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Diagnosis geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class DiagnosisManager implements BasicManager<Diagnosis> {

//    private DatabaseMapper<Diagnosis> diagnosisMapper; FIXME
    private DiagnosisConnector diagnosisMapper;
    private PerfectDiagnosisDiagnosesToAnswersConnector perfectDiagnosisMapper;
    private AnswerToDiagnosisScoreDistributionConnector atdsdc;
    private DiagnosisDescriptionConnector diagnosisDescriptionConnector;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public DiagnosisManager() {
//        diagnosisMapper = new DiagnosisMapper(); FIXME
        diagnosisMapper = new DiagnosisConnector();
        perfectDiagnosisMapper = new PerfectDiagnosisDiagnosesToAnswersConnector();
        atdsdc = new AnswerToDiagnosisScoreDistributionConnector();
        diagnosisDescriptionConnector = new DiagnosisDescriptionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public DiagnosisManager(DiagnosisConnector mapper) {
        diagnosisMapper = mapper;
    }

    @Override
    public Diagnosis add() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosisMapper.create(diagnosis);
        addDescriptions(diagnosis);

        return diagnosis;
    }

    /**
     * Mit dieser Methode werden der Diagnosis in allen Sprachen eine leere Description hinzugefügt.
     *
     * @param diagnosis die Diagnosis
     */
    private void addDescriptions(Diagnosis diagnosis) {
        if (new LanguageManager().getAll() != null) {
            for (Language language : new LanguageManager().getAll()) {
                DiagnosisDescription diagnosisDescription = new DiagnosisDescription();
                diagnosisDescription.setDescription("");
                diagnosisDescription.setDiagnosis(diagnosis);
                diagnosisDescription.setLanguage(language);
                new DiagnosisDescriptionManager().add(diagnosisDescription);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Attribute die nicht vom Objekt in der Datenbank geholt werden falls sie leer sind:
     * <ul>
     * <li>Answers for PerfectDiagnosis</li>
     * </ul>
     */
    @Override
    public Diagnosis update(Diagnosis diagnosis) {
        Diagnosis oldDiagnosis = getAndFetch(diagnosis);
        if (oldDiagnosis == null) {
            throw new IllegalArgumentException("Diagnosis " + diagnosis.getId() + " doesn't exist");
        } else {

            if (diagnosis.getDescriptions() == null) {
                diagnosis.setDescriptions(oldDiagnosis.getDescriptions());
            }
            if (diagnosis.getName() == null) {
                diagnosis.setName(oldDiagnosis.getName());
            }

            addPerfectAnswersToDiagnosis(diagnosis);

            return diagnosisMapper.update(diagnosis);
        }
    }

    @Override
    public Diagnosis get(int id) {
        return diagnosisMapper.read(id);
    }

    @Override
    public ArrayList<Diagnosis> getAll() {
        return diagnosisMapper.readAll();
    }

    @Override
    public void remove(int id) {

        atdsdc.deleteFromDiagnosis(id);
        perfectDiagnosisMapper.deleteFromDiagnosis(id);
        diagnosisDescriptionConnector.deleteFromDiagnosis(id);
        diagnosisMapper.delete(id);
    }

    public Diagnosis diagnose(User user) {
        DiagnosisCalculator calculator = new DiagnosisCalculator();
//        return calculator.getDiagnosis(user);
        return calculator.getDiagnosis(); // RE
    }

    public void addPerfectAnswersToDiagnosis(Diagnosis diagnosis){

        perfectDiagnosisMapper.delete(diagnosis.getId());

        for(Answer a: diagnosis.getAnswersForPerfectDiagnosis())
            perfectDiagnosisMapper.create(a, diagnosis);

    }

    /**
     * Diese Methode wird aufgerufen um das Resultat eines Fragendurchlauf in Form einer Rangliste von Diagnosen zu holen.
     *
     * @param user der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @return Die Diagnosen
     */
    public TreeMap<Diagnosis, Integer> getDiagnosesRankingList(User user) {
        DiagnosisCalculator calculator = new DiagnosisCalculator();
//        return calculator.getDiagnosisRankingList(user);
        return calculator.getSortedRankingList();
    }

    /**
     * Diese Methode wird aufgerufen um die Perfekte diagnosis einer Diagnose zu testen.
     *
     * @param diagnosisId die Id der zu testende Diagnose
     * @return Die Diagnosenrangliste
     */
    public TreeMap<Diagnosis, Integer> testPerfectDiagnosis(int diagnosisId) {
        final Diagnosis diagnosis = get(diagnosisId);

        DiagnosisCalculator calculator = new DiagnosisCalculator();
        return calculator.getPerfectDiagnosis(diagnosis);
    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der Diagnosis zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param diagnosis Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public Diagnosis getAndFetch(Diagnosis diagnosis) {
        return diagnosis;
//        return diagnosisMapper.readAndFetch(diagnosis.getId());
    }

    /**
     * Diese Methode wird aufgerufen um alle Perfekte Diagnosen zu testen.
     *
     * Sie wurde geschrieben weil wir gemerkt haben, dass es mühsam ist, wenn man Perfekte Diagnosen nur einzeln testen muss.
     * @return Alle Diagnosen dessen Tests fehlgeschlagen sind.
     */
    public ArrayList<Diagnosis> testAllPerfectDiagnosis() {
        final ArrayList<Diagnosis> diagnoses = diagnosisMapper.readAll();
        ArrayList<Diagnosis> failedDiagnoses = new ArrayList();

        for(int i=0; i<diagnoses.size();i++) {
            final Diagnosis diagnosis = diagnoses.get(i);

            final TreeMap<Diagnosis, Integer> diagnosisIntegerTreeMap = testPerfectDiagnosis(diagnosis.getId());
            if(!diagnosisIntegerTreeMap.firstKey().equals(diagnosis)){
                failedDiagnoses.add(diagnosis);
            }
        }

        return failedDiagnoses;
    }
}
