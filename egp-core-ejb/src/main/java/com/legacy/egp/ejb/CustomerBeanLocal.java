package com.legacy.egp.ejb;

import com.legacy.egp.entity.Customer;

import javax.ejb.Local;
import java.util.List;

/**
 * Legacy Customer Bean Local Interface
 */
@Local
public interface CustomerBeanLocal {
    
    Customer createCustomer(Customer customer);
    
    Customer updateCustomer(Customer customer);
    
    Customer getCustomer(Long customerId);
    
    Customer getCustomerByNumber(String customerNumber);
    
    List<Customer> searchCustomersByLastName(String lastName);
    
    List<Customer> getActiveCustomers();
    
    void deactivateCustomer(Long customerId);
}