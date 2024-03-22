package com.fdmgroup.java.timothy_chai_project_ecommerce.app;

import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.CartDAO;
import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.CartItemDAO;
import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.CustomerDAO;
import com.fdmgroup.java.timothy_chai_project_ecommerce.repository.ProductDAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Runner {

	public static void main(String[] args) {
		
		// Create DAO instances
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySQL");
		CustomerDAO customerDAO = new CustomerDAO(emf);
		CartDAO cartDAO = new CartDAO(emf);
		CartItemDAO cartItemDAO = new CartItemDAO(emf);
		ProductDAO productDAO = new ProductDAO(emf);
		
		// Create customer instances
		// (String username, String password, String email, String address, String fullName, String cardNumber)
		Customer customer1 = new Customer("customer1", "password1", "email1", "address1", "fullName1", "cardNumber1");
		Customer customer2 = new Customer("customer2", "password2", "email2", "address2", "fullName2", "cardNumber2");
		
		// Create product instances
		Product product1 = new Product("product1", 10, "product1.html", 5.00);
		Product product2 = new Product("product2", 10, "product2.html", 7.00);
		

		productDAO.persist(product1);
		productDAO.persist(product2);
		
		// Add products to carts
		CartItem cartItem1 = new CartItem(product1, 5);
		cartItem1.setCart(customer1.getCart()); // Explicitly set cart for cartItem1
		customer1.getCart().addToCart(cartItem1);

		CartItem cartItem2 = new CartItem(product2, 3);
		cartItem2.setCart(customer2.getCart()); // Explicitly set cart for cartItem2
		customer2.getCart().addToCart(cartItem2);
		
//		// Persist instances
		customerDAO.persist(customer1);
		customerDAO.persist(customer2);	

		
//		cartDAO.addProductToCart(customer1.getCart(), cartItem1);
		

	}

}
