package com.legacy.egp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Legacy Case Record Entity - JPA 1.0 style
 * Represents case records in the EGP system
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@Entity
@Table(name = "egp_case_records")
@NamedQueries({
    @NamedQuery(
        name = "CaseRecord.findByCaseNumber",
        query = "SELECT c FROM CaseRecord c WHERE c.caseNumber = :caseNumber"
    ),
    @NamedQuery(
        name = "CaseRecord.findByCustomer",
        query = "SELECT c FROM CaseRecord c WHERE c.customer.customerId = :customerId ORDER BY c.createdDate DESC"
    ),
    @NamedQuery(
        name = "CaseRecord.findByStatus",
        query = "SELECT c FROM CaseRecord c WHERE c.status = :status ORDER BY c.priority DESC, c.createdDate ASC"
    ),
    @NamedQuery(
        name = "CaseRecord.findByAssignedUser",
        query = "SELECT c FROM CaseRecord c WHERE c.assignedTo = :userId AND c.status IN ('OPEN', 'IN_PROGRESS') ORDER BY c.priority DESC"
    ),
    @NamedQuery(
        name = "CaseRecord.findOpenCases",
        query = "SELECT c FROM CaseRecord c WHERE c.status IN ('OPEN', 'IN_PROGRESS') ORDER BY c.priority DESC, c.createdDate ASC"
    )
})
public class CaseRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Legacy: String constants for status values (before enums were widely used)
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_CLOSED = "CLOSED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";
    
    public static final String TYPE_BENEFITS = "BENEFITS";
    public static final String TYPE_COMPLAINT = "COMPLAINT";
    public static final String TYPE_INQUIRY = "INQUIRY";
    public static final String TYPE_APPEAL = "APPEAL";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Long caseId;
    
    @Column(name = "case_number", unique = true, nullable = false, length = 20)
    private String caseNumber;
    
    // Legacy: ManyToOne with eager fetching (performance issue)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "case_type", nullable = false, length = 30)
    private String caseType;
    
    @Column(name = "priority", length = 10)
    private String priority = PRIORITY_MEDIUM;
    
    @Column(name = "status", length = 20)
    private String status = STATUS_OPEN;
    
    @Column(name = "subject", nullable = false, length = 200)
    private String subject;
    
    @Column(name = "description")
    @Lob // Legacy: Using LOB for large text
    private String description;
    
    @Column(name = "assigned_to")
    private Long assignedTo;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    @Column(name = "modified_by")
    private Long modifiedBy;
    
    @Column(name = "closed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;
    
    @Column(name = "resolution")
    @Lob
    private String resolution;
    
    // Legacy: Bidirectional relationship with cascade (can cause issues)
    @OneToMany(mappedBy = "caseRecord", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Note> notes;
    
    // Constructors
    public CaseRecord() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }
    
    public CaseRecord(String caseNumber, Customer customer, String caseType, String subject) {
        this();
        this.caseNumber = caseNumber;
        this.customer = customer;
        this.caseType = caseType;
        this.subject = subject;
    }
    
    // Legacy: PrePersist and PreUpdate callbacks
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        
        // Legacy: Auto-generate case number if not set
        if (this.caseNumber == null) {
            this.caseNumber = generateCaseNumber();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = new Date();
        
        // Legacy: Auto-set closed date when status changes to closed
        if (STATUS_CLOSED.equals(this.status) && this.closedDate == null) {
            this.closedDate = new Date();
        }
    }
    
    // Legacy: Business logic in entity (anti-pattern)
    private String generateCaseNumber() {
        // Simple case number generation (not thread-safe!)
        return "CASE-" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public Long getCaseId() {
        return caseId;
    }
    
    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }
    
    public String getCaseNumber() {
        return caseNumber;
    }
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getCaseType() {
        return caseType;
    }
    
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public Long getModifiedBy() {
        return modifiedBy;
    }
    
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public Date getClosedDate() {
        return closedDate;
    }
    
    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    // Legacy: Business methods in entity
    public boolean isOpen() {
        return STATUS_OPEN.equals(status) || STATUS_IN_PROGRESS.equals(status);
    }
    
    public boolean isClosed() {
        return STATUS_CLOSED.equals(status);
    }
    
    public boolean isHighPriority() {
        return PRIORITY_HIGH.equals(priority) || PRIORITY_URGENT.equals(priority);
    }
    
    public int getDaysOpen() {
        Date endDate = closedDate != null ? closedDate : new Date();
        long diffInMillies = endDate.getTime() - createdDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }
    
    public String getFormattedStatus() {
        if (status == null) return "Unknown";
        return status.replace("_", " ");
    }
    
    // Legacy: equals and hashCode based on business key
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CaseRecord that = (CaseRecord) obj;
        return caseNumber != null ? caseNumber.equals(that.caseNumber) : that.caseNumber == null;
    }
    
    @Override
    public int hashCode() {
        return caseNumber != null ? caseNumber.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "CaseRecord{" +
                "caseId=" + caseId +
                ", caseNumber='" + caseNumber + '\'' +
                ", caseType='" + caseType + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}