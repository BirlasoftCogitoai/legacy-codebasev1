package com.legacy.egp.dao;

import com.legacy.egp.entity.CaseRecord;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Legacy Case Data Access Object
 */
@Stateless
public class CaseDAO {
    
    private static final Logger logger = Logger.getLogger(CaseDAO.class);
    
    @PersistenceContext(unitName = "egp-pu")
    private EntityManager entityManager;
    
    public CaseRecord create(CaseRecord caseRecord) {
        logger.info("Creating new case: " + caseRecord.getCaseNumber());
        entityManager.persist(caseRecord);
        entityManager.flush();
        return caseRecord;
    }
    
    public CaseRecord update(CaseRecord caseRecord) {
        logger.info("Updating case: " + caseRecord.getCaseNumber());
        return entityManager.merge(caseRecord);
    }
    
    public CaseRecord findById(Long caseId) {
        return entityManager.find(CaseRecord.class, caseId);
    }
    
    public CaseRecord findByCaseNumber(String caseNumber) {
        try {
            Query query = entityManager.createNamedQuery("CaseRecord.findByCaseNumber");
            query.setParameter("caseNumber", caseNumber);
            return (CaseRecord) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseRecord> findByCustomerId(Long customerId) {
        Query query = entityManager.createNamedQuery("CaseRecord.findByCustomer");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseRecord> findByStatus(String status) {
        Query query = entityManager.createNamedQuery("CaseRecord.findByStatus");
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseRecord> findOpenCases() {
        Query query = entityManager.createNamedQuery("CaseRecord.findOpenCases");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseRecord> findByAssignedUser(Long userId) {
        Query query = entityManager.createNamedQuery("CaseRecord.findByAssignedUser");
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}