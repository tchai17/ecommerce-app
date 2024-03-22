package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Cart;
import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Responsible for all persisting all Cart instances, as well as adding and
 * removing items from Carts
 * 
 * @author - timothy.chai
 * 
 * @see Cart
 * 
 */
public class CartDAO implements DAO {

	/**
	 * The EntityManagerFactory instance is reponsible for the creation of
	 * EntityManager instances, which in turn are used for all CRUD operations
	 */
	private EntityManagerFactory emf;

	/**
	 * Main constructor used for the CartDAO class
	 * 
	 * @param emf EntityManagerFactory object used for persistence
	 */
	public CartDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Persists a Cart class instance onto the database
	 * 
	 * @param cart Cart to be persisted
	 * @see Cart
	 */
	public void persist(Cart cart) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(cart);
		entityManager.getTransaction().commit();
	}

	/**
	 * Adds a CartItem to a Cart Calls Cart.addToCart
	 * 
	 * @param cart Cart to which the CartItem is to be added
	 * @param item CartItem to be added
	 * 
	 * @see Cart
	 * @see Cart#addToCart(CartItem)
	 * @see CartItem
	 */
	public void addProductToCart(Cart cart, CartItem item) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		cart.addToCart(item);
		entityManager.getTransaction().commit();
	}

	/**
	 * Removes a CartItem from the Cart Calls Cart.removeFromCart
	 * 
	 * @param cart Cart which CartItem should be removed from
	 * @param item CartItem to remove
	 * 
	 * @see Cart
	 * @see Cart#removeFromCart(CartItem)
	 * @see CartItem
	 */
	public void removeProductFromCart(Cart cart, CartItem item) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		cart.removeFromCart(item);
		entityManager.getTransaction().commit();
	}

}
