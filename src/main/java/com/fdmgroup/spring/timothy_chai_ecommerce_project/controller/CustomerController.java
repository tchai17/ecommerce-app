package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.HashSet;
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
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartItemService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.LikeService;
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

	@Autowired
	private LikeService likeService;

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
		logger.debug("Main page loaded");
		return "index";
	}

	/**
	 * This method is used to handle the register-customer request.
	 * 
	 * @return the registerCustomer view
	 */
	@GetMapping("/register-customer")
	public String registerCustomer() {
		logger.debug("Customer clicked on Register Account button");
		return "registerCustomer";
	}

	/**
	 * This method is used to handle the login request
	 * 
	 * @return the customerLogin view
	 */
	@GetMapping("/login")
	public String loginCustomer() {
		logger.debug("Customer clicked on login button");
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
			httpSession.setAttribute("likes", currentCustomer.getLikes());
			httpSession.setAttribute("isLoggedIn", true);
			logger.debug("Cart is set for this current session");
			logger.debug("Customer redirected to dashboard");

			return "redirect:/product/dashboard";

		} else {
			logger.info("Password input does not match username: " + existingCustomers.get(0).getUsername());
			logger.debug("Customer redirected back to login page");
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
	public String addToCart(@SessionAttribute Customer customer, int productID, @RequestParam int quantity) {
		// Get relevant product and customer that is logged in
		logger.debug("Customer requests to add product (ID: " + productID + ")-quantity: " + quantity);
		Optional<Product> product = productService.findProductById(productID);

		// if product is not found, redirect to dashboard
		if (product.isEmpty()) {
			logger.info("Product not found, please check productID: " + productID);
			return "redirect:/product/dashboard";
		}

		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();
		logger.debug("Customer details retrieved from database: " + target);

		// if product is found
		if (product.isPresent()) {
			// Get customer's cart and set as session attribute, ensure initialization
			logger.debug("Product found: " + product.get());
			Cart cart = target.getCart();
			logger.debug("Cart details retrieved from database: " + cart);
			httpSession.setAttribute("cart", cart);

			// Add to cart and update
			cartService.addToCart(customer, product.get(), quantity);

			// Save as customer
			logger.info("Item added: " + product.get() + " quantity: (" + quantity + ")");
			customer.setCart(cart);
			customerService.updateCustomer(customer);
			logger.debug("Cart updated: " + cart);
			logger.debug("Customer updated: " + customer);
			cart.updateTotalPrice();
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

		logger.debug("Customer requests to remove product (ID: " + productID + ")-quantity: " + quantity);
		Optional<Product> product = productService.findProductById(productID);
		// if product is not found, redirect to dashboard
		if (product.isEmpty()) {
			logger.info("Product not found, please check productID: " + productID);
			return "redirect:/product/dashboard";
		}

		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();
		logger.debug("Customer details retrieved from database: " + target);

		// if product is found
		if (product.isPresent()) {

			logger.debug("Product found: " + product.get());

			// Get customer's cart and set as session attribute
			Cart cart = target.getCart();
			httpSession.setAttribute("cart", cart);
			logger.debug("Cart details retrieved from database: " + cart);

			// Remove from cart and update
			Optional<CartItem> matchingItem = cart.findMatchingCartItem(new CartItem(product.get(), quantity));

			if (matchingItem.isPresent()) {
				logger.debug("Matching item with identical product found: " + matchingItem.get());
				if (quantity >= matchingItem.get().getProductQuantity()) {
					logger.debug(
							"Requested quantity-to-remove (" + quantity + ") is greater than matching item quantity"
									+ " (" + matchingItem.get().getProductQuantity() + ")");
					cartService.removeFromCart(customer, product.get(), quantity);
					logger.info("Item removed from cart: " + product.get() + " quantity: (" + quantity + ")");
					cartItemService.deleteCartItemFromDatabase(matchingItem.get());
					logger.debug("CartItem instance is removed from cart_item table: " + matchingItem.get());
				} else {
					cartService.removeFromCart(customer, product.get(), quantity);
					logger.info("Item removed: " + product.get() + " quantity: (" + quantity + ")");
				}

			}
			cart.updateTotalPrice();

			// Save as customer
			cartService.updateCart(cart);
			logger.debug("Cart updated: " + cart);
			target.setCart(cart);
			customerService.updateCustomer(target);
			logger.debug("Customer updated: " + customer);

		}
		return "redirect:/product/dashboard";
	}

	/**
	 * This method is used to update the quantity of an item in the cart. It
	 * retrieves the customer information from the session, updates the quantity of
	 * the item, updates the cart, and saves the customer information. Then it
	 * redirects the user to the product dashboard.
	 *
	 * @param customer  the customer information
	 * @param productID the product ID
	 * @param direction the direction of the update, either "plus" or "minus"
	 * @return the redirect to the product dashboard
	 */
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
			} else {
				removeFromCart(customer, productID, quantity);
			}

			cart.updateTotalPrice();

			// Save as customer
			customer.setCart(cart);
			customerService.updateCustomer(target);

		}
		return "redirect:/product/dashboard";
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
	@PostMapping("/cartCheckout")
	public String cartCheckout(@SessionAttribute Customer customer) {
		// Get customer's cart and set as session attribute
		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();
		Cart cart = target.getCart();
		httpSession.setAttribute("cart", cart);
		logger.debug("Cart details retrieved from database: " + cart);

		// Save a copy of the items to be deleted from the database
		Set<CartItem> itemsToDelete = new HashSet<>(cart.getItems());
		logger.debug("Items to delete: " + itemsToDelete);
		cartService.checkoutCart(cart);

		// Delete entries from the cart_item table
		itemsToDelete.forEach(item -> {
			logger.debug(item + " has been deleted");
			cartItemService.deleteCartItemFromDatabase(item);
		});

		logger.info("Cart " + target + " is checked out");

		// Save as customer
		customerService.updateCustomer(target);
		logger.debug("Customer updated: " + target);

		return "redirect:/product/dashboard";
	}

	@PostMapping("/addToLikes")
	public String addToLikes(@SessionAttribute Customer customer, @RequestParam int productID) {

		Customer currentCustomer = customerService.findCustomerByID(customer.getCustomerID()).get();
		httpSession.setAttribute("customer", currentCustomer);

		Product chosenProduct = productService.findProductById(productID).get();

		likeService.addToLikes(currentCustomer, chosenProduct);
		customerService.updateCustomer(currentCustomer);
		productService.updateProduct(chosenProduct);

		return "redirect:/product/dashboard";
	}

	@PostMapping("/removeFromLikes")
	public String removeFromLikes(@SessionAttribute Customer customer, @RequestParam int productID) {
		Customer currentCustomer = customerService.findCustomerByID(customer.getCustomerID()).get();
		httpSession.setAttribute("customer", currentCustomer);

		Product chosenProduct = productService.findProductById(productID).get();

		likeService.removeFromLikes(currentCustomer, chosenProduct);
		customerService.updateCustomer(currentCustomer);
		productService.updateProduct(chosenProduct);

		return "redirect:/product/dashboard";
	}

}