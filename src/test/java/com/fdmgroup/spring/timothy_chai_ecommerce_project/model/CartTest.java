package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartTest {

	// Instances
	Cart cart;
	CartItem cartItem1;
	CartItem cartItem2;
	Product product1;
	Product product2;

	@BeforeEach
	void setUp() {
		cart = new Cart();
		product1 = new Product("product1", 99, "img1.html", 5.00);
		product2 = new Product("product2", 99, "img2.html", 6.00);
		cartItem1 = new CartItem(product1, 2);
		cartItem2 = new CartItem(product2, 3);
	}

	@Test
	@DisplayName("1. Test adding matching item will update quantity")
	public void addToCart_WhenCartHasProduct_ThenUpdateQuantity() {
		// Arrange
		List<CartItem> items = new ArrayList<>();
		items.add(cartItem1);
		cart.setItems(items);

		// Act
		cart.addToCart(cartItem1);

		// Assert
		assertEquals(4, cartItem1.getProductQuantity());
	}

	@Test
	@DisplayName("2. Test adding to empty cart will add to list")
	public void addToCart_WhenCartDoesNotHaveProduct_ThenAddProduct() {
		// Arrange

		// Act
		cart.addToCart(cartItem1);
		cart.addToCart(cartItem2);

		// Assert
		assertTrue(cart.getItems().contains(cartItem1));
		assertTrue(cart.getItems().contains(cartItem2));
	}

	@Test
	@DisplayName("3. Test adding to cart which does not have item will add to list")
	public void addToCart_WhenCartHasDifferentProduct_ThenAddProduct() {
		// Arrange
		List<CartItem> items = new ArrayList<>();
		items.add(cartItem1);
		cart.setItems(items);

		// Act
		cart.addToCart(cartItem2);

		// Assert
		assertTrue(cart.getItems().contains(cartItem2));
	}

	@Test
	@DisplayName("4. Test removing from cart removes item from list")
	public void testRemoveFromCart() {
		// Arrange
		List<CartItem> items = new ArrayList<>();
		items.add(cartItem1);
		items.add(cartItem2);
		cart.setItems(items);

		// Act
		cart.removeFromCart(cartItem1);

		// Assert
		assertEquals(1, cart.getItems().size());
		assertEquals(3, cart.getItems().iterator().next().getProductQuantity());
	}

	@Test
	@DisplayName("5. Test findMatchingCartItem returns correct CartItem")
	public void findMatchingCartItem_withMatchingProduct_shouldReturnCartItem() {
		// Arrange
		List<CartItem> items = new ArrayList<>();
		items.add(cartItem1);
		cart.setItems(items);

		// Act
		Optional<CartItem> result = cart.findMatchingCartItem(cartItem1);

		// Assert

		assertTrue(result.isPresent());
		assertEquals(cartItem1, result.get());
	}

	@Test
	@DisplayName("6. Test findMatchingCartItem does not return item when item is missing")
	public void findMatchingCartItem_withNonMatchingProduct_shouldReturnEmpty() {
		// Arrange
		List<CartItem> items = new ArrayList<>();
		items.add(cartItem2);
		cart.setItems(items);
		CartItem testItem = new CartItem(new Product(), 0);

		// Act
		Optional<CartItem> result = cart.findMatchingCartItem(testItem);

		// Assert
		assertFalse(result.isPresent());
	}

//	@Test
//	@DisplayName("7. Test checkout removes all items from cart and sets price to zero")
//	public void testCheckout() {
//		// Arrange
//		Set<CartItem> items = new HashSet<>();
//		items.add(cartItem1);
//		items.add(cartItem2);
//		cart.setItems(items);
//
//		// Act
//		cart.checkout();
//
//		// Assert
//		assertEquals(0, cart.getItems().size());
//		assertEquals(0, cart.getTotalPrice(), 0);
//	}

}
