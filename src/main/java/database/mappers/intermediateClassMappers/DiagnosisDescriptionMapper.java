package database.mappers.intermediateClassMappers;

import database.mappers.DatabaseMapper;
import database.mappers.DatabaseMapperException;
import database.mappers.DatabaseTransaction;
import models.intermediateClassModels.DiagnosisDescription;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Datenbanktabelle "diagnosis_descriptions"
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link database.mappers.DatabaseMapper}
 *
 * @author Oliver Frischknecht
 */
public class DiagnosisDescriptionMapper extends DatabaseTransaction implements DatabaseMapper<DiagnosisDescription> {

    @Override
    public int create(DiagnosisDescription diagnosisDescription) {
        Session session = beginTransaction();
        try {
            Integer id;
            List diagnosisDescriptions = session.createQuery("FROM DiagnosisDescription dd WHERE dd.id = :id")
                    .setInteger("id", diagnosisDescription.getId())
                    .list();
            if (diagnosisDescriptions.size() > 0) {
                throw new DatabaseMapperException("DiagnosisDescription already exists call DiagnosisDescriptionMapper.read()");
            } else {
                id = (Integer) session.save(diagnosisDescription);
            }
            commit();
            return id;

        } catch (Exception e) {
            handleError(e);
            return -1;
        } finally {
            endTransaction();
        }
    }

    @Override
    public DiagnosisDescription update(DiagnosisDescription diagnosisDescription) {
        Session session = beginTransaction();
        try {
            session.update(diagnosisDescription);
            commit();
            return diagnosisDescription;

        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public DiagnosisDescription read(int diagnosisDescriptionId) {
        Session session = beginTransaction();
        try {
            List diagnosisDescriptions = getList(diagnosisDescriptionId, session);
            commit();
            return (DiagnosisDescription) diagnosisDescriptions.get(0);
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public DiagnosisDescription readAndFetch(int id) {
        return read(id);
    }

    private List getList(int diagnosisDescriptionId, Session session) throws DatabaseMapperException {
        List diagnosisDescriptions = session.createQuery("FROM DiagnosisDescription dd WHERE dd.id = :id")
                .setInteger("id", diagnosisDescriptionId)
                .list();
        if (diagnosisDescriptions.size() < 1) {
            throw new DatabaseMapperException("DiagnosisDescription doesn't exist");
        }
        return diagnosisDescriptions;
    }

    @Override
    public ArrayList<DiagnosisDescription> readAll() {
        Session session = beginTransaction();
        try {
            Integer id;
            List diagnosisDescriptions = session.createQuery("FROM DiagnosisDescription").list();
            if (diagnosisDescriptions.size() < 1) {
                throw new DatabaseMapperException("There are currently no DiagnosisDescriptions!");
            }
            commit();
            return (ArrayList<DiagnosisDescription>) diagnosisDescriptions;
        } catch (Exception e) {
            handleError(e);
            return null;
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(int diagnosisDescriptionId) {
        Session session = beginTransaction();
        try {
            List diagnosisDescriptions = getList(diagnosisDescriptionId, session);
            session.delete((DiagnosisDescription) diagnosisDescriptions.get(0));
            commit();
        } catch (Exception e) {
            handleError(e);
        } finally {
            endTransaction();
        }
    }
}
