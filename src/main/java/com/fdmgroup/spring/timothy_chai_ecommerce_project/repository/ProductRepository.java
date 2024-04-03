package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;

/**
 * This interface provides methods for interacting with the database table
 * "product".
 * 
 * @author timothy.chai
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	/**
	 * This method returns a list of products whose product name matches the given
	 * parameter.
	 * 
	 * @param productName the product name to search for
	 * @return a list of products whose product name matches the given parameter
	 */
	public List<Product> findByProductName(String productName);

	public List<Product> findByCategory(String category);

}