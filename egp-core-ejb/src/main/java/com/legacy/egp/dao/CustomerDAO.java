package com.legacy.egp.dao;

import com.legacy.egp.entity.Customer;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Legacy Customer Data Access Object
 * Uses JPA 1.0 EntityManager for database operations
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@Stateless
public class CustomerDAO {
    
    private static final Logger logger = Logger.getLogger(CustomerDAO.class);
    
    @PersistenceContext(unitName = "egp-pu")
    private EntityManager entityManager;
    
    /**
     * Create a new customer
     */
    public Customer create(Customer customer) {
        logger.info("Creating new customer: " + customer.getCustomerNumber());
        
        try {
            entityManager.persist(customer);
            entityManager.flush(); // Legacy: Force immediate persistence
            
            logger.info("Customer created successfully with ID: " + customer.getCustomerId());
            return customer;
            
        } catch (Exception e) {
            logger.error("Error creating customer: " + customer.getCustomerNumber(), e);
            throw new RuntimeException("Failed to create customer", e);
        }
    }
    
    /**
     * Update an existing customer
     */
    public Customer update(Customer customer) {
        logger.info("Updating customer: " + customer.getCustomerNumber());
        
        try {
            Customer updated = entityManager.merge(customer);
            entityManager.flush();
            
            logger.info("Customer updated successfully: " + customer.getCustomerNumber());
            return updated;
            
        } catch (Exception e) {
            logger.error("Error updating customer: " + customer.getCustomerNumber(), e);
            throw new RuntimeException("Failed to update customer", e);
        }
    }
    
    /**
     * Find customer by ID
     */
    public Customer findById(Long customerId) {
        logger.debug("Finding customer by ID: " + customerId);
        
        try {
            Customer customer = entityManager.find(Customer.class, customerId);
            
            if (customer != null) {
                logger.debug("Customer found: " + customer.getCustomerNumber());
            } else {
                logger.debug("Customer not found with ID: " + customerId);
            }
            
            return customer;
            
        } catch (Exception e) {
            logger.error("Error finding customer by ID: " + customerId, e);
            throw new RuntimeException("Failed to find customer", e);
        }
    }
    
    /**
     * Find customer by customer number
     */
    public Customer findByCustomerNumber(String customerNumber) {
        logger.debug("Finding customer by number: " + customerNumber);
        
        try {
            Query query = entityManager.createNamedQuery("Customer.findByCustomerNumber");
            query.setParameter("customerNumber", customerNumber);
            
            Customer customer = (Customer) query.getSingleResult();
            logger.debug("Customer found: " + customer.getCustomerNumber());
            return customer;
            
        } catch (NoResultException e) {
            logger.debug("Customer not found with number: " + customerNumber);
            return null;
            
        } catch (Exception e) {
            logger.error("Error finding customer by number: " + customerNumber, e);
            throw new RuntimeException("Failed to find customer", e);
        }
    }
    
    /**
     * Find customer by SSN (legacy security issue!)
     */
    public Customer findBySSN(String ssn) {
        logger.debug("Finding customer by SSN: " + maskSSN(ssn));
        
        try {
            Query query = entityManager.createNamedQuery("Customer.findBySSN");
            query.setParameter("ssn", ssn);
            
            Customer customer = (Customer) query.getSingleResult();
            logger.debug("Customer found by SSN: " + customer.getCustomerNumber());
            return customer;
            
        } catch (NoResultException e) {
            logger.debug("Customer not found with SSN: " + maskSSN(ssn));
            return null;
            
        } catch (Exception e) {
            logger.error("Error finding customer by SSN", e);
            throw new RuntimeException("Failed to find customer", e);
        }
    }
    
    /**
     * Search customers by last name (partial match)
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findByLastName(String lastName) {
        logger.debug("Searching customers by last name: " + lastName);
        
        try {
            Query query = entityManager.createNamedQuery("Customer.findByLastName");
            query.setParameter("lastName", "%" + lastName + "%");
            
            List<Customer> customers = query.getResultList();
            logger.debug("Found " + customers.size() + " customers with last name: " + lastName);
            return customers;
            
        } catch (Exception e) {
            logger.error("Error searching customers by last name: " + lastName, e);
            throw new RuntimeException("Failed to search customers", e);
        }
    }
    
    /**
     * Get all active customers
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findActiveCustomers() {
        logger.debug("Finding all active customers");
        
        try {
            Query query = entityManager.createNamedQuery("Customer.findActiveCustomers");
            
            List<Customer> customers = query.getResultList();
            logger.debug("Found " + customers.size() + " active customers");
            return customers;
            
        } catch (Exception e) {
            logger.error("Error finding active customers", e);
            throw new RuntimeException("Failed to find active customers", e);
        }
    }
    
    /**
     * Get customers with pagination (legacy approach)
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findCustomersWithPaging(int startPosition, int maxResults) {
        logger.debug("Finding customers with paging: start=" + startPosition + ", max=" + maxResults);
        
        try {
            Query query = entityManager.createQuery(
                "SELECT c FROM Customer c ORDER BY c.lastName, c.firstName"
            );
            query.setFirstResult(startPosition);
            query.setMaxResults(maxResults);
            
            List<Customer> customers = query.getResultList();
            logger.debug("Found " + customers.size() + " customers in page");
            return customers;
            
        } catch (Exception e) {
            logger.error("Error finding customers with paging", e);
            throw new RuntimeException("Failed to find customers", e);
        }
    }
    
    /**
     * Count total customers
     */
    public Long countCustomers() {
        logger.debug("Counting total customers");
        
        try {
            Query query = entityManager.createQuery("SELECT COUNT(c) FROM Customer c");
            Long count = (Long) query.getSingleResult();
            
            logger.debug("Total customer count: " + count);
            return count;
            
        } catch (Exception e) {
            logger.error("Error counting customers", e);
            throw new RuntimeException("Failed to count customers", e);
        }
    }
    
    /**
     * Delete customer (soft delete by changing status)
     */
    public void delete(Long customerId) {
        logger.info("Soft deleting customer with ID: " + customerId);
        
        try {
            Customer customer = findById(customerId);
            if (customer != null) {
                customer.setStatus("INACTIVE");
                update(customer);
                logger.info("Customer soft deleted: " + customer.getCustomerNumber());
            } else {
                logger.warn("Cannot delete - customer not found with ID: " + customerId);
            }
            
        } catch (Exception e) {
            logger.error("Error deleting customer with ID: " + customerId, e);
            throw new RuntimeException("Failed to delete customer", e);
        }
    }
    
    /**
     * Legacy: Direct SQL query example (not recommended)
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findCustomersByState(String state) {
        logger.debug("Finding customers by state: " + state);
        
        try {
            // Legacy: Native SQL query (bypasses JPA)
            Query query = entityManager.createNativeQuery(
                "SELECT * FROM egp_customers WHERE state = ? AND status = 'ACTIVE' ORDER BY last_name",
                Customer.class
            );
            query.setParameter(1, state);
            
            List<Customer> customers = query.getResultList();
            logger.debug("Found " + customers.size() + " customers in state: " + state);
            return customers;
            
        } catch (Exception e) {
            logger.error("Error finding customers by state: " + state, e);
            throw new RuntimeException("Failed to find customers by state", e);
        }
    }
    
    /**
     * Legacy: Batch update example
     */
    public int updateCustomerStatusByState(String state, String newStatus) {
        logger.info("Batch updating customer status for state: " + state + " to: " + newStatus);
        
        try {
            Query query = entityManager.createQuery(
                "UPDATE Customer c SET c.status = :newStatus WHERE c.state = :state"
            );
            query.setParameter("newStatus", newStatus);
            query.setParameter("state", state);
            
            int updatedCount = query.executeUpdate();
            logger.info("Updated " + updatedCount + " customers in state: " + state);
            return updatedCount;
            
        } catch (Exception e) {
            logger.error("Error batch updating customers in state: " + state, e);
            throw new RuntimeException("Failed to batch update customers", e);
        }
    }
    
    // Legacy: Utility method to mask SSN for logging
    private String maskSSN(String ssn) {
        if (ssn == null || ssn.length() < 4) {
            return "***";
        }
        return "***-**-" + ssn.substring(ssn.length() - 4);
    }
}