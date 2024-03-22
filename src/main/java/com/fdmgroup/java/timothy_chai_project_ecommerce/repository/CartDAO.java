package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Cart;
import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CartDAO implements DAO {

	private EntityManagerFactory emf;

	public CartDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public void persist(Cart cart) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(cart);
		entityManager.getTransaction().commit();
	}

	public void addProductToCart(Cart cart, CartItem item) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		cart.addToCart(item);
		entityManager.getTransaction().commit();
	}

	public void removeProductFromCart(Cart cart, CartItem item) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		cart.removeFromCart(item);
		entityManager.getTransaction().commit();
	}

}
