package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository mockOrderRepo;

	@Mock
	private CustomerRepository mockCustomerRepo;

//	@InjectMocks
	private OrderService orderService;

	Customer customer;
	Cart cart;

	@BeforeEach
	public void setup() {
		orderService = new OrderService(mockOrderRepo, mockCustomerRepo);
		customer = new Customer("customer1", "password", "customer@email.com", "address1", "Customer", "1234");
		cart = new Cart();
	}

	@Test
	@DisplayName("1. Test that save calls OrderRepository.save")
	public void testSaveOrder() {
		// arrange
		Order order = new Order();
		when(mockOrderRepo.save(any(Order.class))).thenReturn(order);

		// act
		Order savedOrder = orderService.save(order);

		// assert
		assertEquals(order, savedOrder);
	}

	@Test
	@DisplayName("2. Test that createOrder saves correct items to orderedItems")
	public void testCreateOrder() {
		// arrange

		cart.addToCart(new CartItem(new Product(), 1));
		List<CartItem> items = new ArrayList<CartItem>();
		items.add(new CartItem(new Product(), 1));
		customer.setCart(cart);

		when(mockCustomerRepo.findById(any())).thenReturn(Optional.of(customer));
		when(mockOrderRepo.save(any(Order.class))).thenReturn(new Order());

		// act
		Order createdOrder = orderService.createOrder(customer);

		// assert
		assertSame(items, createdOrder.getOrderedItems());
	}

	@Test
	@DisplayName("3. Test that if customer is not found, empty order should be returned")
	public void testCreateOrderCustomerNotFound() {
		// arrange
		customer.setCustomerID(1);
		when(mockCustomerRepo.findById(any())).thenReturn(Optional.empty());

		// act
		Order createdOrder = orderService.createOrder(customer);

		// assert
		assertEquals(new Order(), createdOrder);
	}

	@Test
	@DisplayName("4. Test that findPopularProducts returns products from past orders")
	public void testFindPopularProducts() {
		// Arrange
		Product product1 = new Product("product1", 99, "image1", 5.00);
		product1.setProductID(1);
		Product product2 = new Product("product2", 99, "image2", 10.00);
		product2.setProductID(2);

		CartItem cartItem1 = new CartItem(product1, 3);
		CartItem cartItem2 = new CartItem(product1, 2);
		CartItem cartItem3 = new CartItem(product2, 4);

		Order order1 = new Order();
		List<CartItem> items1 = new ArrayList<>();
		items1.add(cartItem1);
		items1.add(cartItem2);
		order1.setOrderedItems(items1);

		Order order2 = new Order();
		List<CartItem> items2 = new ArrayList<>();
		items2.add(cartItem3);
		order2.setOrderedItems(items2);

		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		when(mockOrderRepo.findAll()).thenReturn(orders);

		// Act
		Map<Product, Integer> popularProducts = orderService.findPopularProducts();

		// Assert
		assertEquals(2, popularProducts.size());
		assertEquals(5, (int) popularProducts.get(product1)); // 3 + 2
		assertEquals(4, (int) popularProducts.get(product2)); // 4
	}
}
