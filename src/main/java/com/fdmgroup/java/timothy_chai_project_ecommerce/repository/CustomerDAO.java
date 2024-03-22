package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

/**
 * Responsible for CRUD operations involving Customer class instances
 * 
 * @author - timothy.chai
 * 
 * @see Customer
 */
public class CustomerDAO implements DAO {

	/**
	 * The EntityManagerFactory instance is reponsible for the creation of
	 * EntityManager instances, which in turn are used for all CRUD operations
	 */
	private EntityManagerFactory emf;

	/**
	 * Main constructor used for the CustomerDAO class
	 * 
	 * @param emf EntityManagerFactory object used for persistence
	 */
	public CustomerDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Persists a Customer class instance onto the database
	 * 
	 * @param customer Input Customer instance to persist
	 *
	 * @see Customer
	 */
	public void persist(Customer customer) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(customer);
		entityManager.getTransaction().commit();
	}

	/**
	 * Updates a Customer class instance based on an input Customer instance Calls
	 * Customer.updateDetails(Customer)
	 * 
	 * @param customer Input Customer to be used for updating
	 * 
	 * @see Customer#updateDetails(Customer)
	 */
	public void update(Customer customer) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		Customer target = findCustomerById(customer.getCustomerID());
		target.updateDetails(customer);
		entityManager.getTransaction().commit();
	}

	/**
	 * Finds a Customer based on a given ID
	 * 
	 * @param customerId Input ID to search for Customer
	 * @return customer Customer instance that matches given ID
	 * 
	 * @see Customer
	 */
	public Customer findCustomerById(int customerId) {
		EntityManager entityManager = emf.createEntityManager();
		return entityManager.find(Customer.class, customerId);
	}

	/**
	 * Finds a Customer based on a given username
	 * 
	 * @param username Input username string to search for Customer
	 * @return customer Customer instance that matches the given username
	 * 
	 * @see Customer
	 */
	public Customer findCustomerByUsername(String username) {
		EntityManager entityManager = emf.createEntityManager();
		Query searchQuery = entityManager.createQuery("SELECT c FROM Customer c WHERE username LIKE :username");
		searchQuery.setParameter("username", username);
		return (Customer) searchQuery.getSingleResult();

	}

}
