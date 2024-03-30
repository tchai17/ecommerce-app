package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

/**
 * This class provides business logic for managing products in the system. It
 * uses the ProductRepository to store and retrieve products from the database.
 */
@Service
public class ProductService {

	private ProductRepository productRepository;

	/**
	 * Autowire the ProductRepository to allow this class to interact with the
	 * database.
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

		List<Product> existingProducts = productRepository.findByProductName(newProduct.getProductName());
		if (existingProducts.isEmpty()) {
			productRepository.save(newProduct);
		} else {
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
		Optional<Product> targetProduct = productRepository.findById(product.getProductID());
		if (targetProduct.isPresent()) {
			targetProduct.get().setProductName(product.getProductName());
			productRepository.save(targetProduct.get());
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

		Optional<Product> product = productRepository.findById(productId);
		if (product.isPresent()) {
			return product;

		} else {
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
		return productRepository.findByProductName(productName);
	}

	/**
	 * Returns a list of all products in the database.
	 *
	 * @return a list of products
	 */
	public List<Product> returnAllProducts() {
		return productRepository.findAll();
	}

}