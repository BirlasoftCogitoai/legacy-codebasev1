package com.legacy.egp.ejb;

import com.legacy.egp.entity.Customer;

import javax.ejb.Remote;
import java.util.List;

/**
 * Legacy Customer Bean Remote Interface
 */
@Remote
public interface CustomerBeanRemote {
    
    Customer createCustomer(Customer customer);
    
    Customer updateCustomer(Customer customer);
    
    Customer getCustomer(Long customerId);
    
    Customer getCustomerByNumber(String customerNumber);
    
    List<Customer> searchCustomersByLastName(String lastName);
    
    List<Customer> getActiveCustomers();
    
    void deactivateCustomer(Long customerId);
}