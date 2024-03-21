package com.fdmgroup.java.timothy_chai_project_ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartTest {

	Cart cart;
	Product product1;
	CartItem cartItem1;
	
	@BeforeEach
	void setUp() {
		cart = new Cart();
		cart.setCartID(1);
		// public Product(String productName, int stock, String imgURL, float price)
		product1 = new Product("crackers", 5, "hello.html", 5.00);
		product1.setProductID(1);
    }
	
	@Test
	@DisplayName("1. Check the addToCart() method adds correct product to items")
	void testAddToCart_addsCorrectProductToItems() {
		// arrange
		cartItem1 = new CartItem(product1, 1);
		cartItem1.setCart(cart);
		Set<CartItem> actual;
		Set<CartItem> expected = new HashSet<>();
		expected.add(cartItem1);
		// act
		cart.addToCart(cartItem1);
		actual = cart.getItems();
		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
	@DisplayName("2. Check the addToCart() method adds to quantity when product already exists in cart")
	void testAddToCart_addsToQuantityWhenProductAlreadyExistsInCart() {
		// arrange
		Set<CartItem> actual;
		Set<CartItem> expected = new HashSet<>();
		cartItem1 = new CartItem(product1, 2);
		CartItem cartItem2 = new CartItem(product1, 1);
		CartItem cartItem3 = new CartItem(product1, 3);
		expected.add(cartItem3);
		// act
		cart.addToCart(cartItem1);
		cart.addToCart(cartItem2);
		actual = cart.getItems();

		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
    @DisplayName("3. Check the removeFromCart() method removes correct product from items")
    void testRemoveFromCart_removesCorrectProductFromItems() {
		// arrange
		Product product2 = new Product("chocolate", 4, "goodbye.html", 6.00);
		cartItem1 = new CartItem(product1, 2);
		CartItem cartItem2 = new CartItem(product2, 3);
		Set<CartItem> setup = new HashSet<>();
		setup.add(cartItem1);
		setup.add(cartItem2);
		cart.setItems(setup);
		
		Set<CartItem> expected = new HashSet<>();
		expected.add(cartItem1);
		
		Set<CartItem> actual;
		
		// act
		cart.removeFromCart(cartItem2);
		actual = cart.getItems();
		
		// assert
		assertEquals(expected, actual);
		
	}
	
	@Test
	@DisplayName("4. Check the removeFromCart() method removes correct quantity from items")
	void testRemoveFromCart_removesCorrectQuantityFromItems() {
		
		// arrange
		// add cartItem1 to cart
		cartItem1 = new CartItem(product1, 5);
		Set<CartItem> setup = new HashSet<>();
		setup.add(cartItem1);
		cart.setItems(setup);
		
		CartItem cartItem2 = new CartItem(product1, 2);
		Set<CartItem> expected = new HashSet<>();
		expected.add(cartItem2);
		
		Set<CartItem> actual;
		
		// act
		cart.removeFromCart(new CartItem(product1, 3));
		actual = cart.getItems();
		
		// assert
		assertEquals(expected, actual);
	}
	
	
}
