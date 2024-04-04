package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
	private Order order;
	private Customer customer;
	private Cart cart;

	@BeforeEach
	public void setUp() {
		customer = new Customer();
		cart = new Cart();
		order = new Order(cart, customer);
	}

	@Test
	@DisplayName("1. Test that createOrder sets the Customer to the new Order, orderTotalPrice is zero, and orderedItems is empty")
	public void testOrderCreation() {

		// arrange

		// act

		// assert
		assertEquals(customer, order.getCustomer());
		assertEquals(0.0, order.getOrderTotalPrice(), 0.001);
		assertEquals(new ArrayList<>(), order.getOrderedItems());
	}

	@Test
	@DisplayName("2. Test that updateTotalPrice adds the subtotal of each CartItem")
	public void testUpdateOrderTotalPrice() {
		// arrange
		Product product1 = new Product("Product1", 99, "img.url", 20.00);
		Product product2 = new Product("Product2", 99, "img.url", 30.00);

		CartItem cartItem1 = new CartItem(product1, 1);
		CartItem cartItem2 = new CartItem(product2, 1);

		List<CartItem> cartItems = new ArrayList<>();
		cartItems.add(cartItem1);
		cartItems.add(cartItem2);

		cart.setItems(cartItems);

		double item1Subtotal = 20.0;
		double item2Subtotal = 30.0;

		// act
		Order order1 = new Order(cart, customer);
		order1.updateOrderTotalPrice();

		// assert
		assertEquals(item1Subtotal + item2Subtotal, order1.getOrderTotalPrice(), 0.001);
	}

	@Test
	@DisplayName("3. Test that orderDate is set as current date and time")
	public void testOrderDateIsSet() {

		// arrange
		Date currentDate = new Date();
		long currentTime = currentDate.getTime();
		long orderTime = order.getOrderDate().getTime();

		// act

		// assert
		long difference = currentTime - orderTime;
		assertTrue(difference >= 0 && difference <= 1000);
	}

}
