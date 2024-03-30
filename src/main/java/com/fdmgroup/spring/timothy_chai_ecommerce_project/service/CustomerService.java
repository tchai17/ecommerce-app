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

/**
 * This service provides operations for managing customers in the system.
 */
@Service
public class CustomerService {

	private CustomerRepository customerRepo;

	
	/**
     * Autowires the CustomerRepository dependency.
     * @param customerRepo the CustomerRepository instance
     */
	@Autowired
	public CustomerService(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}

	/**
	 * Saves a new customer to the database.
	 * 
	 * @param customer the customer to be saved
	 */
	public void saveNewCustomer(Customer customer) {
		List<Customer> existingCustomers = customerRepo.findByUsername(customer.getUsername());
		if (existingCustomers.isEmpty()) {
			customerRepo.save(customer);
		} else {
			System.out.println("Customer already exists");
		}
	}

	/**
	 * Updates an existing customer in the database.
	 * 
	 * @param customer the customer to be updated
	 */
	public void updateCustomer(Customer customer) {
		Optional<Customer> targetCustomer = customerRepo.findById(customer.getCustomerID());

		// Check if the customer exists in the database
		if (targetCustomer.isPresent()) {
			// Update the customer in the database
			customerRepo.save(customer);
		}
	}

	/**
	 * Returns an {@link Optional} containing the {@link Customer} with the
	 * specified customer ID, if present in the database.
	 *
	 * @param customerID the ID of the customer to be retrieved
	 * @return an {@link Optional} containing the customer with the specified ID, if
	 *         present
	 */
	public Optional<Customer> findCustomerByID(int customerID) {
		Optional<Customer> customer = customerRepo.findById(customerID);

		// Check if the customer exists in the database
		if (customer.isPresent()) {
			return customer;
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Returns a list of customers whose username matches the specified customer
	 * username.
	 *
	 * @param customerName the username of the customer to be matched
	 * @return a list of customers whose username matches the specified customer
	 *         username
	 */
	public List<Customer> findCustomerByUsername(String customerName) {
		return customerRepo.findByUsername(customerName);
	}

	/**
	 * Returns a list of all customers in the database.
	 * 
	 * @return a list of all customers in the database
	 */
	public List<Customer> returnAllCustomers() {
		return customerRepo.findAll();
	}

	/**
	 * Adds a new product to the customer's cart.
	 * 
	 * @param customer the customer who is adding the product to their cart
	 * @param product  the product that is being added to the cart
	 * @param quantity the quantity of the product that is being added to the cart
	 */
	public void addToCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		cart.addToCart(new CartItem(product, quantity));
		this.updateCustomer(customer);
	}

	/**
	 * Removes a product from the customer's cart.
	 * 
	 * @param customer the customer who is removing the product from their cart
	 * @param product  the product that is being removed from the cart
	 * @param quantity the quantity of the product that is being removed from the
	 *                 cart
	 */
	public void removeFromCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		cart.removeFromCart(new CartItem(product, quantity));
		this.updateCustomer(customer);
	}

	/**
	 * Checks out the customer's cart.
	 * 
	 * @param customer the customer whose cart is being checked out
	 */
	public void checkoutCart(Customer customer) {
		customer.getCart().checkout();
		this.updateCustomer(customer);
	}

}
