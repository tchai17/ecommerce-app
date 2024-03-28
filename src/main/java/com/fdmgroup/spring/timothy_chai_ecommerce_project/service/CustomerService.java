package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;



@Service
public class CustomerService {

	private CustomerRepository customerRepo;
	
	@Autowired
	public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

	public void saveCustomer(Customer customer) {
		customerRepo.save(customer);
	}
	
	public Optional<Customer> findCustomerByID(int customerID) {
		Optional<Customer> customer = customerRepo.findById(customerID);
		
		if ( customer.isPresent() ) {
			return customer;
		}
		else {
			return Optional.empty();
		}
	}
	
	public List<Customer> findCustomerByUsername(String customerName) {
		return customerRepo.findByUsername(customerName);
	}
	
	public List<Customer> returnAllCustomers() {
        return customerRepo.findAll();
    }
	
}
