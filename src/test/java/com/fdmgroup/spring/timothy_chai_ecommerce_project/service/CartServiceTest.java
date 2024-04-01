package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	CartService cartService;
	Cart cart;

	@Mock
	CartRepository mockCartRepo;

	@BeforeEach
	public void setUp() {
		cartService = new CartService(mockCartRepo);
		cart = new Cart();
	}

	@Test
	@DisplayName("1. Test when updateCart is called, it calls CartRepository.save()")
	public void test_updateCart_calls_CartRepoSave() {

		// Arrange
		Mockito.when(mockCartRepo.save(cart)).thenReturn(cart);
		Mockito.when(mockCartRepo.findById(cart.getCartID())).thenReturn(Optional.ofNullable(cart));

		// Act
		cartService.updateCart(cart);
		// Assert
		Mockito.verify(mockCartRepo).save(cart);

	}

	@Test
	@DisplayName("2. Test when findMatchingCartItem is called, it returns the matching cart item")
	public void test_findMatchingCartItem_returns_correctItem() {

		// Arrange
		Product product1 = new Product("Product1", 99, "img.url", 15.00);
		Product product2 = new Product("Product2", 99, "img.url", 10.00);
		CartItem cartItem1 = new CartItem(product1, 1);
		CartItem cartItem2 = new CartItem(product2, 3);
		Set<CartItem> items = new HashSet<CartItem>();
		items.add(cartItem1);
		items.add(cartItem2);
		cart.setItems(items);
		Optional<CartItem> expected = Optional.ofNullable(cartItem1);

		// Act
		Optional<CartItem> actual = cartService.findCartItemInCart(cart, product1);

		// Assert
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("3. Test when findMatchingCartItem is called, it returns an empty Optional if no match is found")
	public void test_findMatchingCartItem_returnsEmpty_whenNoMatch() {
		// Arrange
		Product product1 = new Product("Product1", 99, "img.url", 15.00);
		Product product2 = new Product("Product2", 99, "img.url", 10.00);
		CartItem cartItem1 = new CartItem(product1, 1);
		Set<CartItem> items = new HashSet<CartItem>();
		items.add(cartItem1);

		cart.setItems(items);
		Optional<CartItem> expected = Optional.empty();

		// Act
		Optional<CartItem> actual = cartService.findCartItemInCart(cart, product2);

		// Assert
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("4. Test when addToCart is called, it calls CartRepository.save()")

	public void testAddToCart() {
		// Arrange
		Product product1 = new Product("Product1", 99, "img.url", 15.00);
		Cart cart = new Cart();
		Customer customer = new Customer();
		customer.setCart(cart);
		CartItem cartItem1 = new CartItem(product1, 1);
		Set<CartItem> items = new HashSet<CartItem>();
		items.add(cartItem1);
		Mockito.when(mockCartRepo.findById(cart.getCartID())).thenReturn(Optional.ofNullable(cart));

		// Act
		cartService.addToCart(customer, product1, 1);
		Set<CartItem> actual = customer.getCart().getItems();
		// Assert

//		assertEquals(items, actual);
		assertTrue(customer.getCart().getItems().contains(cartItem1));

	}
}
