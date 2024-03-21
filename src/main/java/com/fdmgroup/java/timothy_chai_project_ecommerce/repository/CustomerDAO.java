package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

public class CustomerDAO implements DAO{
	
	private EntityManagerFactory emf;
	
    public CustomerDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    
    public void persist(Customer customer) {
    	EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
    }
    
    public void update(Customer customer) {
    	EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Customer target = findCustomerById(customer.getCustomerID());
        target.updateDetails(customer);
        entityManager.getTransaction().commit();
    }
    
    public Customer findCustomerById(int customerId) {
    	EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Customer customer = entityManager.find(Customer.class, customerId);
        entityManager.getTransaction().commit();
        return customer;
    }
    
    public Customer findCustomerByUsername(String username) {
    	EntityManager entityManager = emf.createEntityManager();
    	Query searchQuery = entityManager.createQuery("SELECT c FROM Customer c WHERE username LIKE :username");
    	searchQuery.setParameter("username", username);
    	return (Customer) searchQuery.getSingleResult();
    	
    }
	


	
	
}
