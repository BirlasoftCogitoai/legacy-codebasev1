package com.legacy.egp.dao;

import com.legacy.egp.entity.Note;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Legacy Notes Data Access Object
 */
@Stateless
public class NotesDAO {
    
    private static final Logger logger = Logger.getLogger(NotesDAO.class);
    
    @PersistenceContext(unitName = "egp-pu")
    private EntityManager entityManager;
    
    public Note create(Note note) {
        logger.info("Creating new note for case: " + note.getCaseRecord().getCaseNumber());
        entityManager.persist(note);
        entityManager.flush();
        return note;
    }
    
    public Note update(Note note) {
        return entityManager.merge(note);
    }
    
    public Note findById(Long noteId) {
        return entityManager.find(Note.class, noteId);
    }
    
    @SuppressWarnings("unchecked")
    public List<Note> findByCase(Long caseId) {
        Query query = entityManager.createNamedQuery("Note.findByCase");
        query.setParameter("caseId", caseId);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Note> findPublicNotesByCase(Long caseId) {
        Query query = entityManager.createNamedQuery("Note.findPublicNotesByCase");
        query.setParameter("caseId", caseId);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Note> findByCaseAndType(Long caseId, String noteType) {
        Query query = entityManager.createNamedQuery("Note.findByCaseAndType");
        query.setParameter("caseId", caseId);
        query.setParameter("noteType", noteType);
        return query.getResultList();
    }
}