package com.legacy.egp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Legacy Note Entity - JPA 1.0 style
 * Represents notes attached to case records in the EGP system
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@Entity
@Table(name = "egp_notes")
@NamedQueries({
    @NamedQuery(
        name = "Note.findByCase",
        query = "SELECT n FROM Note n WHERE n.caseRecord.caseId = :caseId ORDER BY n.createdDate DESC"
    ),
    @NamedQuery(
        name = "Note.findByCaseAndType",
        query = "SELECT n FROM Note n WHERE n.caseRecord.caseId = :caseId AND n.noteType = :noteType ORDER BY n.createdDate DESC"
    ),
    @NamedQuery(
        name = "Note.findPublicNotesByCase",
        query = "SELECT n FROM Note n WHERE n.caseRecord.caseId = :caseId AND n.isPrivate = false ORDER BY n.createdDate DESC"
    ),
    @NamedQuery(
        name = "Note.findByCreatedBy",
        query = "SELECT n FROM Note n WHERE n.createdBy = :userId ORDER BY n.createdDate DESC"
    )
})
public class Note implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Legacy: String constants for note types
    public static final String TYPE_GENERAL = "GENERAL";
    public static final String TYPE_INTERNAL = "INTERNAL";
    public static final String TYPE_CUSTOMER_CONTACT = "CUSTOMER_CONTACT";
    public static final String TYPE_SYSTEM = "SYSTEM";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long noteId;
    
    // Legacy: ManyToOne with eager fetching
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "case_id", nullable = false)
    private CaseRecord caseRecord;
    
    @Column(name = "note_text", nullable = false)
    @Lob // Legacy: Using LOB for text content
    private String noteText;
    
    @Column(name = "note_type", length = 20)
    private String noteType = TYPE_GENERAL;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "is_private")
    private Boolean isPrivate = Boolean.FALSE;
    
    // Constructors
    public Note() {
        this.createdDate = new Date();
    }
    
    public Note(CaseRecord caseRecord, String noteText) {
        this();
        this.caseRecord = caseRecord;
        this.noteText = noteText;
    }
    
    public Note(CaseRecord caseRecord, String noteText, String noteType) {
        this(caseRecord, noteText);
        this.noteType = noteType;
    }
    
    // Legacy: PrePersist callback
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        
        // Legacy: Default values
        if (this.noteType == null) {
            this.noteType = TYPE_GENERAL;
        }
        if (this.isPrivate == null) {
            this.isPrivate = Boolean.FALSE;
        }
    }
    
    // Getters and Setters
    public Long getNoteId() {
        return noteId;
    }
    
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
    
    public CaseRecord getCaseRecord() {
        return caseRecord;
    }
    
    public void setCaseRecord(CaseRecord caseRecord) {
        this.caseRecord = caseRecord;
    }
    
    public String getNoteText() {
        return noteText;
    }
    
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
    
    public String getNoteType() {
        return noteType;
    }
    
    public void setNoteType(String noteType) {
        this.noteType = noteType;
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
    
    public Boolean getIsPrivate() {
        return isPrivate;
    }
    
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    
    // Legacy: Convenience methods
    public boolean isPrivate() {
        return Boolean.TRUE.equals(isPrivate);
    }
    
    public boolean isPublic() {
        return !isPrivate();
    }
    
    public boolean isInternalNote() {
        return TYPE_INTERNAL.equals(noteType);
    }
    
    public boolean isCustomerContact() {
        return TYPE_CUSTOMER_CONTACT.equals(noteType);
    }
    
    public String getFormattedNoteType() {
        if (noteType == null) return "General";
        return noteType.replace("_", " ");
    }
    
    public String getTruncatedText(int maxLength) {
        if (noteText == null) return "";
        if (noteText.length() <= maxLength) return noteText;
        return noteText.substring(0, maxLength) + "...";
    }
    
    // Legacy: equals and hashCode based on ID (not ideal)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Note note = (Note) obj;
        return noteId != null ? noteId.equals(note.noteId) : note.noteId == null;
    }
    
    @Override
    public int hashCode() {
        return noteId != null ? noteId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", noteType='" + noteType + '\'' +
                ", isPrivate=" + isPrivate +
                ", createdDate=" + createdDate +
                ", noteText='" + getTruncatedText(50) + '\'' +
                '}';
    }
}