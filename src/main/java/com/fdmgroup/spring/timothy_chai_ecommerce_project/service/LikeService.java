package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

/**
 * This service is responsible for managing customer likes and dislikes of
 * products.
 * 
 * @author - timothy.chai
 * 
 * @see Customer
 */
@Service
public class LikeService {

	private static Logger logger = LogManager.getLogger(LikeService.class);

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	public LikeService(CustomerRepository customerRepo, ProductRepository productRepo) {
		this.customerRepo = customerRepo;
		this.productRepo = productRepo;
	}

	/**
	 * Adds the given product to the list of products liked by the given customer.
	 * 
	 * @param customer the customer who is liking the product
	 * @param product  the product that is being liked
	 */
	public void addToLikes(Customer customer, Product product) {
		logger.debug("addToLikes called for customer: " + customer + ", product: " + product);
		Optional<Customer> optionalCustomer = customerRepo.findById(customer.getCustomerID());
		Optional<Product> optionalProduct = productRepo.findById(product.getProductID());

		if (optionalCustomer.isEmpty()) {
			logger.info("Customer ID not found: " + customer.getCustomerID());
			return;
		}
		if (optionalProduct.isEmpty()) {
			logger.info("Product ID not found: " + product.getProductID());
			return;
		}

		Customer currentCustomer = optionalCustomer.get();
		currentCustomer.addToLikes(optionalProduct.get());
		customerRepo.save(currentCustomer);
		logger.info("Product - " + product + " added to customer - " + customer + " likes");

	}

	/**
	 * Removes the given product from the list of products liked by the given
	 * customer.
	 * 
	 * @param customer the customer who is unliking the product
	 * @param product  the product that is being unliked
	 */
	public void removeFromLikes(Customer customer, Product product) {
		logger.debug("removeFromLikes called for customer: " + customer + ", product: " + product);
		Optional<Customer> optionalCustomer = customerRepo.findById(customer.getCustomerID());
		Optional<Product> optionalProduct = productRepo.findById(product.getProductID());

		if (optionalCustomer.isEmpty()) {
			logger.info("Customer ID not found: " + customer.getCustomerID());
			return;
		}
		if (optionalProduct.isEmpty()) {
			logger.info("Product ID not found: " + product.getProductID());
			return;
		}

		Customer currentCustomer = optionalCustomer.get();
		currentCustomer.removeFromLikes(optionalProduct.get());
		customerRepo.save(currentCustomer);
		logger.info("Product - " + product + " removed from customer - " + customer + " likes");
	}

}