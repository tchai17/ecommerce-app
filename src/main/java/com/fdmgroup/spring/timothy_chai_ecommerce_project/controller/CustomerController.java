package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.List;
import java.util.Optional;

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

	/** The customer service that is used to interact with the customer data. */
	@Autowired
	private CustomerService customerService;

	/** The product service that is used to interact with the product data. */
	@Autowired
	private ProductService productService;

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
		System.out.println("Initiate registration for new customer");

		// Get parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String fullName = request.getParameter("fullName");
		String cardNumber = request.getParameter("cardNumber");

		// Create new Customer instance
		Customer newCustomer = new Customer(username, password, email, address, fullName, cardNumber);

		// Save to DB
		customerService.saveNewCustomer(newCustomer);
		System.out.println(newCustomer);
		System.out.println("Registration Complete");

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
		System.out.println("Initiate login for existing customer");

		// Get parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Check if username already exists in database
		List<Customer> existingCustomers = customerService.findCustomerByUsername(username);
		if (existingCustomers.isEmpty()) {
			System.out.println("Username does not exist in database");
			return "customerLogin";
		}

		// Check if password is correct
		if (existingCustomers.get(0).getPassword().equals(password)) {
			System.out.println("Password is correct");
			Customer currentCustomer = existingCustomers.get(0);
			Cart cart = currentCustomer.getCart(); // Retrieve current cart

			System.out.println(cart);

			httpSession.setAttribute("customer", currentCustomer);
			httpSession.setAttribute("cart", cart);
			httpSession.setAttribute("isLoggedIn", true);
			return "redirect:/product/dashboard";
		} else {
			System.out.println("Password is incorrect");
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
	@Transactional(readOnly = false)
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
			cart.addToCart(new CartItem(product.get(), quantity));
			cart.updateTotalPrice();

			// Save as customer
			customer.setCart(cart);
			customerService.updateCustomer(customer);
			System.out.println("Item added");
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
	@Transactional(readOnly = false)
	@PostMapping("/removeFromCart")
	public String removeFromCart(@SessionAttribute Customer customer, @RequestParam String productID,
			@RequestParam(value = "productQuantity") String quantity) {

		int realProductID = Integer.parseInt(productID);
		int realQuantity = Integer.parseInt(quantity);

		Optional<Product> product = productService.findProductById(realProductID);
		Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();

		// if product is found
		if (product.isPresent()) {

			// Get customer's cart and set as session attribute
			Cart cart = target.getCart();
			httpSession.setAttribute("cart", cart);

			// Remove from cart and update
			cart.removeFromCart(new CartItem(product.get(), realQuantity));
			cart.updateTotalPrice();

			// Save as customer
			customer.setCart(cart);
			customerService.updateCustomer(customer);
			System.out.println("Item removed");
		}
		return "redirect:/product/dashboard";
	}

}