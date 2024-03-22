package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CartItemDAO implements DAO {

	private EntityManagerFactory emf;

	public CartItemDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public void persist(CartItem cartItem) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(cartItem);
		entityManager.getTransaction().commit();
	}

	public void remove(CartItem cartItem) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(cartItem);
		entityManager.getTransaction().commit();
	}

	public CartItem findById(int cartId) {
		EntityManager entityManager = emf.createEntityManager();
		CartItem cartItem = entityManager.find(CartItem.class, cartId);
		return cartItem;

	}
}
