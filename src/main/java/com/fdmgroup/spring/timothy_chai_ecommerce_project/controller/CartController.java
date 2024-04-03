package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.OrderService;

@Controller
@Transactional(readOnly = false)
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

//	private HttpSession session;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/checkout")
	public String submitOrder(@SessionAttribute Customer customer, Model model) {
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			return "customerLogin";
		} else {
			Customer currentCustomer = optionalCustomer.get();
			Cart cart = currentCustomer.getCart();
			orderService.createOrder(currentCustomer);
			cartService.clearCart(cart);
			cartService.updateCart(cart);
			currentCustomer.setCart(cart);
			customerService.updateCustomer(currentCustomer);
			return "redirect:/product/dashboard";
		}

	}

	/**
	 * Process cart checkout for the logged-in customer. Retrieves the customer's
	 * cart from the session attribute, checks out the cart, deletes the cart items
	 * from the database, updates the customer information, and redirects to the
	 * product dashboard page.
	 *
	 * @param customer The logged-in customer retrieved from session attribute.
	 * @return A redirection to the product dashboard page.
	 */

}
