package com.legacy.egp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Legacy Customer Entity - JPA 1.0 style
 * Represents customer information in the EGP system
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@Entity
@Table(name = "egp_customers")
@NamedQueries({
    @NamedQuery(
        name = "Customer.findByCustomerNumber",
        query = "SELECT c FROM Customer c WHERE c.customerNumber = :customerNumber"
    ),
    @NamedQuery(
        name = "Customer.findByLastName",
        query = "SELECT c FROM Customer c WHERE UPPER(c.lastName) LIKE UPPER(:lastName) ORDER BY c.lastName, c.firstName"
    ),
    @NamedQuery(
        name = "Customer.findBySSN",
        query = "SELECT c FROM Customer c WHERE c.ssn = :ssn"
    ),
    @NamedQuery(
        name = "Customer.findActiveCustomers",
        query = "SELECT c FROM Customer c WHERE c.status = 'ACTIVE' ORDER BY c.lastName, c.firstName"
    )
})
public class Customer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "customer_number", unique = true, nullable = false, length = 20)
    private String customerNumber;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    // Legacy: SSN stored as plain text (security issue!)
    @Column(name = "ssn", length = 11)
    private String ssn;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "address_line1", length = 100)
    private String addressLine1;
    
    @Column(name = "address_line2", length = 100)
    private String addressLine2;
    
    @Column(name = "city", length = 50)
    private String city;
    
    @Column(name = "state", length = 2)
    private String state;
    
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";
    
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
    
    // Legacy: Bidirectional relationship (can cause performance issues)
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CaseRecord> caseRecords;
    
    // Constructors
    public Customer() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }
    
    public Customer(String customerNumber, String firstName, String lastName) {
        this();
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Legacy: PrePersist and PreUpdate callbacks
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = new Date();
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerNumber() {
        return customerNumber;
    }
    
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getSsn() {
        return ssn;
    }
    
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddressLine1() {
        return addressLine1;
    }
    
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    public String getAddressLine2() {
        return addressLine2;
    }
    
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public List<CaseRecord> getCaseRecords() {
        return caseRecords;
    }
    
    public void setCaseRecords(List<CaseRecord> caseRecords) {
        this.caseRecords = caseRecords;
    }
    
    // Legacy: Business methods in entity (anti-pattern)
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getFormattedAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) {
            address.append(addressLine1);
        }
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        if (city != null) {
            address.append(", ").append(city);
        }
        if (state != null) {
            address.append(", ").append(state);
        }
        if (zipCode != null) {
            address.append(" ").append(zipCode);
        }
        return address.toString();
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    // Legacy: equals and hashCode based on business key
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Customer customer = (Customer) obj;
        return customerNumber != null ? customerNumber.equals(customer.customerNumber) : customer.customerNumber == null;
    }
    
    @Override
    public int hashCode() {
        return customerNumber != null ? customerNumber.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerNumber='" + customerNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}