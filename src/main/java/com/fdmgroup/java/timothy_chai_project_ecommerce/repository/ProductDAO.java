package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Responsible for persisting, updating and deleting Product instances
 * 
 * @author - timothy.chai
 * 
 *         The ProductDAO class is responsible for persisting, updating, and
 *         deleting all Product instances on the database
 * 
 * @see Product
 * 
 */
public class ProductDAO {

	/**
	 * The EntityManagerFactory instance is reponsible for the creation of
	 * EntityManager instances, which in turn are used for all CRUD operations
	 */
	private EntityManagerFactory emf;

	/**
	 * Main constructor used for the ProductDAO class
	 * 
	 * @param emf EntityManagerFactory object used for persistence
	 */
	public ProductDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Persists a Product class instance onto the database
	 * 
	 * @param product Product to be persisted
	 * @see Product
	 */
	public void persist(Product product) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(product);
		entityManager.getTransaction().commit();
	}

	/**
	 * Removes a managed Product from the database
	 * 
	 * @param product Product to be removed
	 * @see Product
	 */
	public void remove(Product product) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(product);
		entityManager.getTransaction().commit();
	}

	/**
	 * Finds and returns a managed Product
	 * 
	 * @param id ProductID to search for product
	 * @return product Target product specified by the ID input
	 * @see Product
	 */
	public Product findProductById(int id) {
		EntityManager entityManager = emf.createEntityManager();
		Product product = entityManager.find(Product.class, id);
		return product;
	}
}
