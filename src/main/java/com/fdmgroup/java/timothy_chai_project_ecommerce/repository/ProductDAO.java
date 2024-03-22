package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ProductDAO {

	private EntityManagerFactory emf;

	public ProductDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public void persist(Product product) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(product);
		entityManager.getTransaction().commit();
	}

	public void remove(Product product) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(product);
		entityManager.getTransaction().commit();
	}

	public Product findProductById(int id) {
		EntityManager entityManager = emf.createEntityManager();
		Product product = entityManager.find(Product.class, id);
		return product;
	}
}
