package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.List;
import java.util.Optional;

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
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartItemService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This class is the customer controller that handles all the requests related
 * to the customer.
 */
@Controller
@RequestMapping("/customer")
@Transactional(readOnly = false)
public class CustomerController {

	private Logger logger = LogManager.getLogger(CustomerController.class);
	
	/** The customer service that is used to interact with the customer data. */
	@Autowired
	private CustomerService customerService;

	/** The product service that is used to interact with the product data. */
	@Autowired
	private ProductService productService;

	@Autowired
	private CartItemService cartItemService;
	
	
	@Autowired
	private CartService cartService;
	
	/** The session that is used to store the customer information. */
	@Autowired
	private HttpSession httpSession;

	/**
	 * This method is used to handle the index page of the customer.
	 * 
	 * @return the index page of the customer
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * This method is used to handle the register-customer request.
	 * 
	 * @return the registerCustomer view
	 */
	@GetMapping("/register-customer")
	public String registerCustomer() {
		return "registerCustomer";
	}

	/**
	 * This method is used to handle the login request
	 * 
	 * @return the customerLogin view
	 */
	@GetMapping("/login")
	public String loginCustomer() {
		return "customerLogin";
	}

	/**
	 * This method is used to process the customer registration form. It retrieves
	 * the form parameters and creates a new customer object. It then saves the
	 * customer to the database and displays a "registration complete" message.
	 * 
	 * @param request the HTTP request object
	 * @return the "complete" page after registering the customer
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
	 * This method is used to process the customer login form. It retrieves the form
	 * parameters and checks if the username and password match an existing customer
	 * in the database. If the login is successful, the customer's information is
	 * stored in the session and the user is redirected to the product dashboard. If
	 * the login fails, the user is redirected back to the customer login page.
	 * 
	 * @param request the HTTP request object
	 * @return the view to be displayed after login. If login is successful, user is
	 *         redirected to the product dashboard.
	 */
	@PostMapping("/login-customer")
	public String processLogin(HttpServletRequest request) {
		
		logger.debug("Customer clicked on login button");
		logger.debug("Initiate login request");

		// Get parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		logger.debug("Customer username and password received");
		logger.debug("Start verification");

		// Check if username already exists in database
		List<Customer> existingCustomers = customerService.findCustomerByUsername(username);
		if (existingCustomers.isEmpty()) {
			
			logger.info("Username not found in database");
			logger.debug("Redirecting user back to login page");
			return "customerLogin";
		}

		// Check if password is correct
		if (existingCustomers.get(0).getPassword().equals(password)) {
			Customer currentCustomer = existingCustomers.get(0);
			logger.info("Password input is verified for the username: " + currentCustomer.getUsername());
			Cart cart = currentCustomer.getCart(); // Retrieve current cart

			logger.debug("Retrieving cart information: " + cart);

			httpSession.setAttribute("customer", currentCustomer);
			httpSession.setAttribute("cart", cart);
			httpSession.setAttribute("isLoggedIn", true);
			return "redirect:/product/dashboard";
		} else {
			logger.info("Password input does not match username: " + existingCustomers.get(0).getUsername());
			return "customerLogin";
		}
	}

	/**
	 * This method is used to add an item to the cart. It retrieves the customer
	 * information from the session, adds the item to the cart, updates the cart,
	 * and saves the customer information. Then it redirects the user to the product
	 * dashboard.
	 * 
	 * @param customer  the customer information
	 * @param productId the product ID
	 * @param quantity  the quantity of the item
	 * @return the redirect to the product dashboard
	 */
	@PostMapping("/addToCart")
	public String addToCart(@SessionAttribute Customer customer, int productId, @RequestParam int quantity) {
		// Get relevant product and customer that is logged in
		Optional<Product> product = productService.findProductById(productId);
		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();

		// if product is found
		if (product.isPresent()) {
			// Get customer's cart and set as session attribute
			Cart cart = target.getCart();
			httpSession.setAttribute("cart", cart);

			// Add to cart and update
			CartItem newItem = new CartItem(product.get(), quantity);
			cart.addToCart(newItem);
			cart.updateTotalPrice();

			// Save as customer
			customer.setCart(cart);
			customerService.updateCustomer(customer);
			System.out.println("Item " + newItem + " added");
		}
		return "redirect:/product/dashboard";
	}

	/**
	 * This method is used to remove an item from the cart. It retrieves the
	 * customer information from the session, removes the item from the cart,
	 * updates the cart, and saves the customer information. Then it redirects the
	 * user to the product dashboard.
	 * 
	 * @param customer  the customer information
	 * @param productID the product ID
	 * @param quantity  the quantity of the item to be removed
	 * @return the redirect to the product dashboard
	 */
	@PostMapping("/removeFromCart")
	public String removeFromCart(@SessionAttribute Customer customer, @RequestParam int productID,
			@RequestParam(value = "productQuantity") int quantity) {
		
		Optional<Product> product = productService.findProductById(productID);
		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();

		// if product is found
		if (product.isPresent()) {

			// Get customer's cart and set as session attribute
			customerService.updateCustomer(target);
			Cart cart = target.getCart();
			httpSession.setAttribute("cart", cart);

			// Remove from cart and update
			Optional<CartItem> matchingItem = cart.findMatchingCartItem(new CartItem(product.get(), quantity));
			
			if (matchingItem.isPresent()) {
				if ( quantity >= matchingItem.get().getProductQuantity() ) {
					cart.removeFromCart(new CartItem(product.get(), quantity));
					cartItemService.deleteCartItemFromDatabase(matchingItem.get());
				}
			}
			
			cart.removeFromCart(new CartItem(product.get(), quantity));
			cart.updateTotalPrice();

			// Save as customer
			cartService.updateCart(cart);
			target.setCart(cart);
			customerService.updateCustomer(target);
			System.out.println(cart);
			System.out.println("Item removed");
		}
		System.out.println();
		return "redirect:/product/dashboard";
	}
	
	
	@PostMapping("/updateCartItemQuantity")
	public String updateCartItemQuantity(@SessionAttribute Customer customer, @RequestParam int productID,
            @RequestParam(value = "updateQuantity") String direction) {
		
		Optional<Product> product = productService.findProductById(productID);
		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();
		int quantity = 1;
		
		// if product is found
		if (product.isPresent()) {

			// Get customer's cart and set as session attribute
			Cart cart = target.getCart();
			httpSession.setAttribute("cart", cart);
			
			// Get direction
			if (direction.equals("plus")) {
				addToCart(customer, productID, quantity);
			}
			else {
				removeFromCart(customer, productID, quantity);
			}
			System.out.println("Total price updated - ");
			cart.updateTotalPrice();

			// Save as customer
			customer.setCart(cart);
			customerService.updateCustomer(target);
			System.out.println("Item quantity updated - ");
		}
		return "redirect:/product/dashboard";
	}

}