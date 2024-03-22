package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Responsible for persisting all CartItem class instances
 * 
 * @author - timothy.chai
 * 
 * @see CartItem
 */
public class CartItemDAO implements DAO {

	/**
	 * The EntityManagerFactory instance is reponsible for the creation of
	 * EntityManager instances, which in turn are used for all CRUD operations
	 */
	private EntityManagerFactory emf;

	/**
	 * Main constructor used for the CartItemDAO class
	 * 
	 * @param emf EntityManagerFactory object used for persistence
	 */
	public CartItemDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Persists a CartItem class instance onto the database
	 * 
	 * @param cartItem CartItem to be persisted
	 * @see CartItem
	 */
	public void persist(CartItem cartItem) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(cartItem);
		entityManager.getTransaction().commit();
	}

	/**
	 * Removes a CartItem class instance from the database
	 * 
	 * @param cartItem CartItem to be removed
	 * @see CartItem
	 */
	public void remove(CartItem cartItem) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(cartItem);
		entityManager.getTransaction().commit();
	}

	/**
	 * Finds and returns a managed CartItem
	 * 
	 * @param id CartItemID to search for CartItem
	 * @return cartItem Target CartItem specified by the ID input
	 * @see CartItem
	 */
	public CartItem findById(int cartItemId) {
		EntityManager entityManager = emf.createEntityManager();
		CartItem cartItem = entityManager.find(CartItem.class, cartItemId);
		return cartItem;

	}
}
