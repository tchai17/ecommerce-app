/** Holds all classes relevant for running the e-commerce application.
 * 
 */
package com.fdmgroup.java.timothy_chai_project_ecommerce.app;

import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.CustomerDAO;
import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.ProductDAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/** Responsible for initializing all application classes and DAO classes
 * @author - timothy.chai
 * 
 * The Runner class is responsible for initializing all main application classes and DAO classes for the operation of the application
 * @see Customer
 * @see Cart
 * @see CartItem
 * @see Product
 */
public class Runner {

	/** Main runner method for initializing application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Create DAO instances
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySQL");
		CustomerDAO customerDAO = new CustomerDAO(emf);
		ProductDAO productDAO = new ProductDAO(emf);

		// Create customer instances
		Customer customer1 = new Customer("customer1", "password1", "email1", "address1", "fullName1", "cardNumber1");
		Customer customer2 = new Customer("customer2", "password2", "email2", "address2", "fullName2", "cardNumber2");

		// Create product instances
		Product product1 = new Product("product1", 10, "product1.html", 5.00);
		Product product2 = new Product("product2", 10, "product2.html", 7.00);

		productDAO.persist(product1);
		productDAO.persist(product2);

		// Add products to carts
		CartItem cartItem1 = new CartItem(product1, 5);
		cartItem1.setCart(customer1.getCart());
		customer1.getCart().addToCart(cartItem1);

		CartItem cartItem2 = new CartItem(product2, 3);
		cartItem2.setCart(customer2.getCart());
		customer2.getCart().addToCart(cartItem2);

		// Persist instances
		customerDAO.persist(customer1);
		customerDAO.persist(customer2);

	}

}
