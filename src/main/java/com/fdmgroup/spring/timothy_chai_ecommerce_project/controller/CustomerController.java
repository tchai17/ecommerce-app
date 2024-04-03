package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.LikeService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This class is the customer controller that handles all the requests related
 * to the customer. These requests include customer registration, login, adding
 * to likes, and removing from likes
 * 
 * @author - timothy.chai
 * 
 * @see Customer
 * 
 */
@Controller
@RequestMapping("/customer")
@Transactional(readOnly = false)
public class CustomerController {

	private Logger logger = LogManager.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private HttpSession httpSession;

	/**
	 * Handles the request for registering a new customer account.
	 * 
	 * @return The registerCustomer view.
	 */
	@GetMapping("/register-customer")
	public String registerCustomer() {
		logger.debug("Customer clicked on Register Account button");
		return "registerCustomer";
	}

	/**
	 * Handles the login request.
	 * 
	 * @return The customerLogin view.
	 */
	@GetMapping("/login")
	public String loginCustomer() {
		logger.debug("Customer clicked on login button");
		return "customerLogin";
	}

	/**
	 * Processes the customer registration form.
	 * 
	 * @param request The HTTP request object containing registration form data.
	 * @return The "complete" page after registering the customer.
	 */
	@PostMapping("/registration")
	public String processRegistration(HttpServletRequest request) {
		logger.debug("Initiate registration for new customer");

		// Get parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String fullName = request.getParameter("fullName");
		String cardNumber = request.getParameter("cardNumber");

		logger.debug("Customer details received: " + username + " " + address + " " + " " + email);

		// Create new Customer instance
		Customer newCustomer = new Customer(username, password, email, address, fullName, cardNumber);

		// Save to DB
		customerService.saveNewCustomer(newCustomer);
		logger.info("New customer account persisted onto database. " + newCustomer);

		return "complete";
	}

	/**
	 * Processes the customer login form.
	 * 
	 * @param request The HTTP request object containing login form data.
	 * @return The view to be displayed after login. If login is successful, user is
	 *         redirected to the product dashboard.
	 */
	@PostMapping("/login-customer")
	public String processLogin(HttpServletRequest request) {

		logger.debug("Initiate login request");

		// Get parameters from form
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		logger.debug("Customer username and password received");
		logger.debug("Start verification");

		// Check if username already exists in database
		List<Customer> existingCustomers = customerService.findCustomerByUsername(username);
		if (existingCustomers.isEmpty()) {
			logger.info("Username not found in database: " + username);
			logger.debug("Redirecting user back to login page");
			return "customerLogin";
		}

		// Check if username matches only one entry
		if (existingCustomers.size() > 1) {
			logger.info("Username input is not unique: " + username);
			logger.debug("Redirecting user back to login page");
			return "customerLogin";
		}

		// Check if password is correct
		if (!existingCustomers.get(0).getPassword().equals(password)) {
			logger.info("Password input does not match username: " + existingCustomers.get(0).getUsername());
			logger.debug("Customer redirected back to login page");
			return "customerLogin";
		}

		// if password is correct, retrieve customer information from database
		Customer currentCustomer = existingCustomers.get(0);
		logger.info("Password input is verified for the username: " + currentCustomer.getUsername());

		// Retrieve current cart
		Cart cart = currentCustomer.getCart();
		logger.debug("Retrieving cart information: " + cart);

		// Save customer, cart, and likes to session
		httpSession.setAttribute("customer", currentCustomer);
		httpSession.setAttribute("cart", cart);
		httpSession.setAttribute("likes", currentCustomer.getLikes());
//		httpSession.setAttribute("orders", currentCustomer.getOrders());
		logger.debug("Customer likes and orders are set for this session");
		httpSession.setAttribute("isLoggedIn", true);
		logger.debug("Cart is set for this current session");
		logger.debug("Customer redirected to dashboard");

		return "redirect:/product/dashboard";

	}

	/**
	 * Adds a product to the customer's likes.
	 * 
	 * @param customer  The currently logged-in customer retrieved from session
	 *                  attributes.
	 * @param productID The ID of the product to be added to likes.
	 * @return A redirection to the product dashboard page.
	 */
	@PostMapping("/addToLikes")
	public String addToLikes(@SessionAttribute Customer customer, @RequestParam int productID) {
		// Check if customer exists in database
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found in database");
			return "redirect:/customer/login";
		}

		Customer currentCustomer = optionalCustomer.get();
		httpSession.setAttribute("customer", currentCustomer);
		logger.debug("Customer added to session");

		// Check if product exists in database
		Optional<Product> optionalProduct = productService.findProductById(productID);
		if (optionalProduct.isEmpty()) {
			logger.info("Product not found in database");
			return "redirect:/product/dashboard";
		}

		Product chosenProduct = optionalProduct.get();
		logger.debug("Product found in database");

		// Check if product is already in likes
		Set<Product> likes = currentCustomer.getLikes();
		if (likes.contains(chosenProduct)) {
			logger.info("Product already in customer's likes");
			return "redirect:/product/dashboard";
		}

		// add to likes and persist
		likeService.addToLikes(currentCustomer, chosenProduct);
		customerService.updateCustomer(currentCustomer);
		productService.updateProduct(chosenProduct);

		return "redirect:/product/dashboard";
	}

	/**
	 * Removes a product from the customer's likes.
	 * 
	 * @param customer  the currently logged-in customer retrieved from session
	 *                  attributes.
	 * @param productID the ID of the product to be removed from likes.
	 * @return link the redirection to the product dashboard page.
	 */
	@PostMapping("/removeFromLikes")
	public String removeFromLikes(@SessionAttribute Customer customer, @RequestParam int productID) {

		// Check if customer exists in database
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found in database");
			return "redirect:/customer/login";
		}

		// Save customer to session if found
		Customer currentCustomer = optionalCustomer.get();
		httpSession.setAttribute("customer", currentCustomer);
		logger.debug("Customer added to session");

		// Check if product exists in database
		Optional<Product> optionalProduct = productService.findProductById(productID);
		if (optionalProduct.isEmpty()) {
			logger.info("Product not found in database");
			return "redirect:/product/dashboard";
		}

		Product chosenProduct = optionalProduct.get();

		// Check if product is already in likes
		Set<Product> likes = currentCustomer.getLikes();
		if (!likes.contains(chosenProduct)) {
			logger.info("Product not found in likes");
			return "redirect:/product/dashboard";
		}

		// Remove from likes
		likeService.removeFromLikes(currentCustomer, chosenProduct);
		customerService.updateCustomer(currentCustomer);
		productService.updateProduct(chosenProduct);

		return "redirect:/product/dashboard";
	}

	@GetMapping("/orders")
	public String goToOrders(@SessionAttribute Customer customer) {
		// Check if customer exists in database
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found in database");
			return "redirect:/customer/login";
		}
		Customer currentCustomer = optionalCustomer.get();
		httpSession.setAttribute("customer", currentCustomer);
		logger.debug("Customer added to session");
		List<Order> orders = currentCustomer.getOrders();
		orders.forEach(order -> order.updateOrderTotalPrice());
		httpSession.setAttribute("orders", orders);

		return "orders";
	}

}