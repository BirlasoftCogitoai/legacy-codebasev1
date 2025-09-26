package com.legacy.egp.ejb;

import com.legacy.egp.entity.CaseRecord;

import javax.ejb.Local;
import java.util.List;

/**
 * Legacy Case Bean Local Interface
 */
@Local
public interface CaseBeanLocal {
    
    CaseRecord createCase(CaseRecord caseRecord);
    
    CaseRecord updateCase(CaseRecord caseRecord);
    
    CaseRecord getCase(Long caseId);
    
    CaseRecord getCaseByCaseNumber(String caseNumber);
    
    List<CaseRecord> getCasesByCustomer(Long customerId);
    
    List<CaseRecord> getOpenCases();
    
    List<CaseRecord> getCasesByAssignedUser(Long userId);
    
    void closeCase(Long caseId, String resolution);
    
    void assignCase(Long caseId, Long userId);
}