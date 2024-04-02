package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.controller.CustomerController;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

/**
 * This class provides business logic for managing products in the system. It
 * uses the ProductRepository to store and retrieve products from the database.
 */
@Service
public class ProductService {

	private Logger logger = LogManager.getLogger(CustomerController.class);

	private ProductRepository productRepository;

	/**
	 * Autowire the ProductRepository to allow this class to interact with the
	 * database.
	 * 
	 * @see ProductRepository
	 */
	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepository = productRepo;
	}

	/**
	 * Saves a new product to the database.
	 *
	 * @param newProduct the product to save
	 * @see Product
	 */
	public void saveProduct(Product newProduct) {
		logger.debug("saveProduct called for product: " + newProduct);
		List<Product> existingProducts = productRepository.findByProductName(newProduct.getProductName());
		if (existingProducts.isEmpty()) {
			logger.debug("No product with matching productID found, saving new Product");
			productRepository.save(newProduct);
		} else {
			logger.info("Product already exists, abort save");
			System.out.println("Product already exists");
		}

	}

	/**
	 * Updates an existing product in the database.
	 *
	 * @param product the product to update
	 * @see Product
	 */
	public void updateProduct(Product product) {
		logger.debug("updateProduct called for product: " + product);
		Optional<Product> targetProduct = productRepository.findById(product.getProductID());
		if (targetProduct.isPresent()) {
			logger.debug("Product with matching ID found, updating product details");
			Product target = targetProduct.get();
			target.setProductName(product.getProductName());
			target.setPrice(product.getPrice());
			target.setImgURL(product.getImgURL());
			target.setStock(product.getStock());
			productRepository.save(target);
		} else {
			logger.info("No product with matching productID found, try saving new product");
		}
	}

	/**
	 * Finds a product by its ID.
	 *
	 * @param productId the ID of the product to find
	 * @return the product, if found, or an empty {@link Optional}
	 * @see Product
	 */
	public Optional<Product> findProductById(int productId) {
		logger.debug("findProductById called for productID: " + productId);
		Optional<Product> product = productRepository.findById(productId);
		if (product.isPresent()) {
			logger.debug("Product with matching productID found, returning product: " + product);
			return product;

		} else {
			logger.info("No product with matching productID found, returning Optional.empty");
			return Optional.empty();
		}
	}

	/**
	 * Finds products by their name.
	 *
	 * @param productName the name of the product to find
	 * @return a list of products with the given name
	 */
	public List<Product> findProductByProductName(String productName) {
		logger.debug("findProductByProductName called for product name: " + productName);
		return productRepository.findByProductName(productName);
	}

	public List<Product> findProductByCategory(String category) {
		logger.debug("findProductByCategory called for category: " + category);
		return productRepository.findByCategory(category);
	}

	/**
	 * Returns a list of all products in the database.
	 *
	 * @return a list of products
	 */
	public List<Product> returnAllProducts() {
		logger.debug("returnAllProducts called");
		return productRepository.findAll();
	}

	public Set<String> returnAllCategories() {
		logger.debug("returnAllCategories called");
		Set<String> allCategories = new HashSet<>();
		List<Product> allProducts = productRepository.findAll();
		allProducts.forEach(product -> allCategories.add(product.getCategory()));
		return allCategories;
	}

}