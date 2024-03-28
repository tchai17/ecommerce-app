package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;



@Service
public class CustomerService {

	private CustomerRepository customerRepo;
	
	@Autowired
	public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

	public void saveCustomer(Customer customer) {
		List<Customer> existingCustomers = customerRepo.findByUsername(customer.getUsername());
		
        if (existingCustomers.isEmpty()) {
            customerRepo.save(customer);
        }
        else {
        	System.out.println("Username already exists, please input a unique username!");
        }
		
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
	
	public void addToCart(Customer customer, Product product, int quantity) {
		customer.getCart().addToCart(new CartItem(product, quantity));
		this.saveCustomer(customer);
	}
	
	public void removeFromCart(Customer customer, Product product, int quantity) {
		customer.getCart().removeFromCart(new CartItem(product, quantity));
		this.saveCustomer(customer);
	}
	
	public void checkoutCart(Customer customer) {
		customer.getCart().checkout();
        this.saveCustomer(customer);
	}
	
}
