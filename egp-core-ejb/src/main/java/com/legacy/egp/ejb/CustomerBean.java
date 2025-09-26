package com.legacy.egp.ejb;

import com.legacy.egp.dao.CustomerDAO;
import com.legacy.egp.entity.Customer;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Legacy Customer Session Bean - EJB 3.x
 * Provides business logic for customer operations
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CustomerBean implements CustomerBeanLocal, CustomerBeanRemote {
    
    private static final Logger logger = Logger.getLogger(CustomerBean.class);
    
    @EJB
    private CustomerDAO customerDAO;
    
    @Override
    public Customer createCustomer(Customer customer) {
        logger.info("Creating customer: " + customer.getFirstName() + " " + customer.getLastName());
        
        // Legacy: Business validation in EJB
        validateCustomer(customer);
        
        // Legacy: Generate customer number if not provided
        if (customer.getCustomerNumber() == null) {
            customer.setCustomerNumber(generateCustomerNumber());
        }
        
        // Check for duplicate customer number
        Customer existing = customerDAO.findByCustomerNumber(customer.getCustomerNumber());
        if (existing != null) {
            throw new RuntimeException("Customer number already exists: " + customer.getCustomerNumber());
        }
        
        return customerDAO.create(customer);
    }
    
    @Override
    public Customer updateCustomer(Customer customer) {
        logger.info("Updating customer: " + customer.getCustomerNumber());
        
        validateCustomer(customer);
        
        Customer existing = customerDAO.findById(customer.getCustomerId());
        if (existing == null) {
            throw new RuntimeException("Customer not found: " + customer.getCustomerId());
        }
        
        return customerDAO.update(customer);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Customer getCustomer(Long customerId) {
        logger.debug("Getting customer by ID: " + customerId);
        return customerDAO.findById(customerId);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Customer getCustomerByNumber(String customerNumber) {
        logger.debug("Getting customer by number: " + customerNumber);
        return customerDAO.findByCustomerNumber(customerNumber);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Customer> searchCustomersByLastName(String lastName) {
        logger.debug("Searching customers by last name: " + lastName);
        return customerDAO.findByLastName(lastName);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Customer> getActiveCustomers() {
        logger.debug("Getting all active customers");
        return customerDAO.findActiveCustomers();
    }
    
    @Override
    public void deactivateCustomer(Long customerId) {
        logger.info("Deactivating customer: " + customerId);
        
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        
        customer.setStatus("INACTIVE");
        customerDAO.update(customer);
    }
    
    // Legacy: Business validation method
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        // Legacy: Email validation (basic)
        if (customer.getEmail() != null && !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Legacy: SSN validation (basic)
        if (customer.getSsn() != null && customer.getSsn().length() != 11) {
            throw new IllegalArgumentException("SSN must be 11 characters (XXX-XX-XXXX)");
        }
    }
    
    // Legacy: Simple customer number generation (not thread-safe!)
    private String generateCustomerNumber() {
        return "CUST-" + String.format("%06d", System.currentTimeMillis() % 1000000);
    }
}