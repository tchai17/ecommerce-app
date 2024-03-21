package com.fdmgroup.java.timothy_chai_project_ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartTest {

	Cart cart;
	Product product1;
	
	@BeforeEach
	void setUp() {
		cart = new Cart();
		cart.setCartID(1);
		// public Product(String productName, int stock, String imgURL, float price)
		product1 = new Product("crackers", 5, "hello.html", 5.00);
    }
	
	@Test
	@DisplayName("1. Check the addToCart() method adds correct product to items")
	void testAddToCart_addsCorrectProductToItems() {
		// arrange
		Map<Product, Integer> actual;
		Map<Product, Integer> expected = new HashMap<>();
		expected.put(product1, 1);
		// act
		cart.addToCart(product1, 1);
		actual = cart.getItems();
		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
	@DisplayName("2. Check the addToCart() method adds one quantity when product already exists in cart")
	void testAddToCart_addsOneQuantityWhenProductAlreadyExistsInCart() {
		// arrange
		Map<Product, Integer> actual;
		Map<Product, Integer> expected = new HashMap<>();
		expected.put(product1, 2);
		// act
		cart.addToCart(product1, 1);
		cart.addToCart(product1, 1);
		actual = cart.getItems();

		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
    @DisplayName("3. Check the removeFromCart() method removes correct product from items")
    void testRemoveFromCart_removesCorrectProductFromItems() {
		// arrange
		Product product2 = new Product("chocolate", 4, "goodbye.html", 6.00);
		Map<Product, Integer> setupMap = new HashMap<>();
		setupMap.put(product1, 3);
		setupMap.put(product2, 2);
		cart.setItems(setupMap);
		
		Map<Product, Integer> expected = new HashMap<>();
		expected.put(product1, 3);
		
		Map<Product, Integer> actual;
		
		
		// act
		cart.removeFromCart(product2, 2);
		actual = cart.getItems();
		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
	@DisplayName("4. Check the removeFromCart() method removes correct quantity from items")
	void testRemoveFromCart_removesCorrectQuantityFromItems() {
		
		// arrange
		
		Map<Product, Integer> setupMap = new HashMap<>();
		setupMap.put(product1, 3);
		cart.setItems(setupMap);
		
		Map<Product, Integer> expected = new HashMap<>();
		expected.put(product1, 1);
		
		Map<Product, Integer> actual;
		
		// act
		cart.removeFromCart(product1, 2);
		actual = cart.getItems();
		// assert
		assertEquals(expected, actual);
	}
	
	
}
