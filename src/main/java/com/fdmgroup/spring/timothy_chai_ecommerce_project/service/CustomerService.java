package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
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

	
	
	public void saveNewCustomer(Customer customer) {
         List<Customer> existingCustomers = customerRepo.findByUsername(customer.getUsername());
         if ( existingCustomers.isEmpty() ) {
             customerRepo.save(customer);
         }
         else {
             System.out.println("Customer already exists");
         }
		
	}
	
	public void updateCustomer(Customer customer) {
		Optional<Customer> targetCustomer = customerRepo.findById(customer.getCustomerID());
		
        if ( targetCustomer.isPresent() ) {
            customerRepo.save(customer);
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
		Cart cart = customer.getCart();
		cart.addToCart(new CartItem(product, quantity));
		this.updateCustomer(customer);
	}
	
	public void removeFromCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		cart.removeFromCart(new CartItem(product, quantity));
		this.updateCustomer(customer);
	}
	
	public void checkoutCart(Customer customer) {
		customer.getCart().checkout();
        this.updateCustomer(customer);
	}
	
}
