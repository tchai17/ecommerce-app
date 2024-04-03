package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartItemRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private CustomerRepository customerRepo;

	public OrderService(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	public Order save(Order order) {
		return orderRepo.save(order);
	}

	public Order createOrder(Customer customer) {
		Optional<Customer> optionalCustomer = customerRepo.findById(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			return new Order();
		} else {
			Customer currentCustomer = optionalCustomer.get();
			Cart cart = currentCustomer.getCart();
			Order order = new Order(cart, currentCustomer);
			for (CartItem cartItem : cart.getItems()) {
				CartItem newCartItem = new CartItem();
				newCartItem.setProduct(cartItem.getProduct());
				newCartItem.setProductQuantity(cartItem.getProductQuantity());
				newCartItem.setProductSubtotal(cartItem.getProductSubtotal());
				newCartItem.setOrder(order); // Set order reference for the new cart item
				cartItemRepo.save(newCartItem); // Persist new cart item
			}
			orderRepo.save(order);
			return order;
		}

	}

}
