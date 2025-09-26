package com.legacy.egp.ejb;

import com.legacy.egp.dao.CaseDAO;
import com.legacy.egp.dao.CustomerDAO;
import com.legacy.egp.entity.CaseRecord;
import com.legacy.egp.entity.Customer;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Date;
import java.util.List;

/**
 * Legacy Case Management Session Bean
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CaseBean implements CaseBeanLocal, CaseBeanRemote {
    
    private static final Logger logger = Logger.getLogger(CaseBean.class);
    
    @EJB
    private CaseDAO caseDAO;
    
    @EJB
    private CustomerDAO customerDAO;
    
    @Override
    public CaseRecord createCase(CaseRecord caseRecord) {
        logger.info("Creating case for customer: " + caseRecord.getCustomer().getCustomerNumber());
        
        validateCase(caseRecord);
        
        if (caseRecord.getCaseNumber() == null) {
            caseRecord.setCaseNumber(generateCaseNumber());
        }
        
        return caseDAO.create(caseRecord);
    }
    
    @Override
    public CaseRecord updateCase(CaseRecord caseRecord) {
        logger.info("Updating case: " + caseRecord.getCaseNumber());
        validateCase(caseRecord);
        return caseDAO.update(caseRecord);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public CaseRecord getCase(Long caseId) {
        return caseDAO.findById(caseId);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public CaseRecord getCaseByCaseNumber(String caseNumber) {
        return caseDAO.findByCaseNumber(caseNumber);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<CaseRecord> getCasesByCustomer(Long customerId) {
        return caseDAO.findByCustomerId(customerId);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<CaseRecord> getOpenCases() {
        return caseDAO.findOpenCases();
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<CaseRecord> getCasesByAssignedUser(Long userId) {
        return caseDAO.findByAssignedUser(userId);
    }
    
    @Override
    public void closeCase(Long caseId, String resolution) {
        logger.info("Closing case: " + caseId);
        
        CaseRecord caseRecord = caseDAO.findById(caseId);
        if (caseRecord == null) {
            throw new RuntimeException("Case not found: " + caseId);
        }
        
        caseRecord.setStatus(CaseRecord.STATUS_CLOSED);
        caseRecord.setClosedDate(new Date());
        caseRecord.setResolution(resolution);
        
        caseDAO.update(caseRecord);
    }
    
    @Override
    public void assignCase(Long caseId, Long userId) {
        logger.info("Assigning case " + caseId + " to user " + userId);
        
        CaseRecord caseRecord = caseDAO.findById(caseId);
        if (caseRecord == null) {
            throw new RuntimeException("Case not found: " + caseId);
        }
        
        caseRecord.setAssignedTo(userId);
        caseRecord.setStatus(CaseRecord.STATUS_IN_PROGRESS);
        
        caseDAO.update(caseRecord);
    }
    
    private void validateCase(CaseRecord caseRecord) {
        if (caseRecord == null) {
            throw new IllegalArgumentException("Case cannot be null");
        }
        
        if (caseRecord.getSubject() == null || caseRecord.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("Case subject is required");
        }
        
        if (caseRecord.getCaseType() == null || caseRecord.getCaseType().trim().isEmpty()) {
            throw new IllegalArgumentException("Case type is required");
        }
        
        if (caseRecord.getCustomer() == null) {
            throw new IllegalArgumentException("Customer is required");
        }
    }
    
    private String generateCaseNumber() {
        return "CASE-" + new Date().getYear() + "-" + String.format("%06d", System.currentTimeMillis() % 1000000);
    }
}