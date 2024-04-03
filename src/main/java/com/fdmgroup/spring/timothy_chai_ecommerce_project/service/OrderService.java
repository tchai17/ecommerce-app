package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.OrderRepository;

/**
 * This service class is used to create and manage orders.
 * 
 * @author timothy.chai
 * 
 * @see Order
 *
 */
@Service
public class OrderService {

	private static Logger logger = LogManager.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private CustomerRepository customerRepo;

	/**
	 * Saves the given order to the database.
	 * 
	 * @param order
	 * @return
	 */
	public Order save(Order order) {
		return orderRepo.save(order);
	}

	/**
	 * Creates a new order for the given customer.
	 * 
	 * @param customer
	 * @return
	 */
	public Order createOrder(Customer customer) {
		logger.debug("createOrder called for customer " + customer);

		Optional<Customer> optionalCustomer = customerRepo.findById(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("customer is not found in database - please verify: " + customer);
			return new Order();
		}
		Customer currentCustomer = optionalCustomer.get();
		Cart cart = currentCustomer.getCart();
		logger.info("customer found in database - getting cart information");

		// add items of cart to order
		Order order = new Order(cart, currentCustomer);
		order.updateOrderTotalPrice();
		logger.info("New order is created");

		orderRepo.save(order);
		return order;
	}

	public Map<Product, Integer> findPopularProducts() {
		logger.debug("findPopularProducts called");
		List<Order> allOrders = orderRepo.findAll();
		logger.debug("Finding all orders in OrderRepository");
		Map<Product, Integer> productMap = new HashMap<>();
		BiFunction<Integer, Integer, Integer> addValue = (currentValue, amountToAdd) -> currentValue + amountToAdd;
		for (Order order : allOrders) {
			for (CartItem cartItem : order.getOrderedItems()) {
				Product product = cartItem.getProduct();
				int quantity = cartItem.getProductQuantity();
				productMap.merge(product, quantity, addValue);
			}
		}
		logger.info("Popular product map is created");
		return productMap;
	}

}
