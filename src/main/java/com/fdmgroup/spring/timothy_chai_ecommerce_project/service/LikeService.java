package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

@Service
public class LikeService {

	private Logger logger = LogManager.getLogger(LikeService.class);

	private CustomerRepository customerRepo;

	private ProductRepository productRepo;

	public LikeService(CustomerRepository customerRepo, ProductRepository productRepo) {
		this.customerRepo = customerRepo;
		this.productRepo = productRepo;
	}

	public void addToLikes(Customer customer, Product product) {
		Optional<Customer> targetCustomer = customerRepo.findById(customer.getCustomerID());
		Optional<Product> targetProduct = productRepo.findById(product.getProductID());

		if (targetCustomer.isPresent() && targetProduct.isPresent()) {
			Customer currentCustomer = targetCustomer.get();
			currentCustomer.addToLikes(targetProduct.get());
			customerRepo.save(currentCustomer);
		}

	}

	public void removeFromLikes(Customer customer, Product product) {
		Optional<Customer> targetCustomer = customerRepo.findById(customer.getCustomerID());
		Optional<Product> targetProduct = productRepo.findById(product.getProductID());

		if (targetCustomer.isPresent() && targetProduct.isPresent()) {
			Customer currentCustomer = targetCustomer.get();
			currentCustomer.removeFromLikes(targetProduct.get());
			customerRepo.save(currentCustomer);
		}
	}

}
