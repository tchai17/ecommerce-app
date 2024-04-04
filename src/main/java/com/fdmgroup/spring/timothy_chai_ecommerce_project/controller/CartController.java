package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartItemService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CartService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.OrderService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is the controller responsible for handling all cart-related
 * requests, including adding to the cart, removing from the cart, and checking
 * out
 * 
 * @author - timothy.chai
 * 
 * @see Cart
 *
 */
@Controller
@Transactional(readOnly = false)
public class CartController {

	private Logger logger = LogManager.getLogger(CartController.class);

	/**
	 * This is the autowired CartService instance.
	 */
	@Autowired
	private CartService cartService;

	/**
	 * This is the autowired OrderService instance.
	 */
	@Autowired
	private OrderService orderService;

	/**
	 * This is the autowired CustomerService instance.
	 */
	@Autowired
	private CustomerService customerService;

	/**
	 * This is the autowired ProductService instance.
	 */
	@Autowired
	private ProductService productService;

	/**
	 * This is the autowired CartItemService instance.
	 */
	@Autowired
	private CartItemService cartItemService;

	/**
	 * This is the autowired HttpSession instance.
	 */
	@Autowired
	private HttpSession session;

	/**
	 * This is the index page for the cart. It is simply a redirect to the cart
	 * page.
	 * 
	 * @return A redirect to the cart page.
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * This method is called when the user clicks on the 'Add to Cart' button
	 * located either under each product display, or from the 'likes' list
	 * 
	 * This method is used to add an item to the cart. It retrieves the customer
	 * information from the session, adds the item to the cart, updates the cart,
	 * and saves the customer information. Then it redirects the user to the product
	 * dashboard.
	 * 
	 * @param customer  the customer instance who made the request
	 * @param productId the product ID of the product to be added
	 * @param quantity  the quantity of the product to be added
	 * @return link redirect to the product dashboard
	 */
	@PostMapping("/addToCart")
	public String addToCart(@SessionAttribute Customer customer, int productID, @RequestParam int quantity) {

		// Get relevant product and customer that is logged in from database
		logger.debug("Customer requests to add product (ID: " + productID + ")-quantity: " + quantity);
		Optional<Product> optionalProduct = productService.findProductById(productID);

		// if product is not found, redirect to dashboard
		if (optionalProduct.isEmpty()) {
			logger.info("Product not found, please check productID: " + productID);
			return "redirect:/product/dashboard";
		}

		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());

		// if customer is not found, redirect to dashboard
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found, please check customerID: " + customer.getCustomerID());
			return "redirect:/product/dashboard";
		}

		// if product and customer is found, proceed to add product to cart
		Customer currentCustomer = optionalCustomer.get();
		Product product = optionalProduct.get();
		logger.debug("Customer details retrieved from database: " + currentCustomer);
		logger.debug("Product found: " + product);

		// Get customer's cart and set as session attribute, avoid lazy initialization
		// exception
		Cart cart = currentCustomer.getCart();
		logger.debug("Cart details retrieved from database: " + cart);
		session.setAttribute("cart", cart);

		// if quantity is zero or there is no stock, do not allow user to add to cart
		if (quantity < 1 || product.getStock() < 1) {
			return "redirect:/product/dashboard";
		}
		// Add to cart and update
		cartService.addToCart(customer, product, quantity);
		logger.info("Item added: " + product + " quantity: (" + quantity + ")");

		// Save as customer
		customer.setCart(cart);
		customerService.updateCustomer(customer);
		logger.debug("Cart updated: " + cart);
		logger.debug("Customer updated: " + customer);
		cart.updateTotalPrice();

		return "redirect:/product/dashboard";
	}

	/**
	 * This method is called when the user clicks on any of the 'Remove From Cart'
	 * buttons located next to each product
	 * 
	 * This method is used to remove an item from the cart. It retrieves the
	 * customer information from the session, removes the item from the cart,
	 * updates the cart, and saves the customer information. Then it redirects the
	 * user to the product dashboard.
	 * 
	 * @param customer  the customer instance who made the request
	 * @param productID the product ID of the product to be removed
	 * @param quantity  the quantity of the item to be removed
	 * @return link redirect to the product dashboard
	 */
	@PostMapping("/removeFromCart")
	public String removeFromCart(@SessionAttribute Customer customer, @RequestParam int productID,
			@RequestParam(value = "productQuantity") int quantity) {

		logger.debug("Customer requests to remove product (ID: " + productID + ")-quantity: " + quantity);

		// if product is not found, redirect to dashboard
		Optional<Product> optionalProduct = productService.findProductById(productID);
		if (optionalProduct.isEmpty()) {
			logger.info("Product not found, please check productID: " + productID);
			return "redirect:/product/dashboard";
		}

		// if customer is not found, redirect to dashboard
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found, please check customerID: " + customer.getCustomerID());
			return "redirect:/product/dashboard";
		}

		Customer target = optionalCustomer.get();
		Product product = optionalProduct.get();
		logger.debug("Customer details retrieved from database: " + target);
		logger.debug("Product found: " + product);

		// if product and customer is found, proceed to remove from cart

		// Get customer's cart and set as session attribute, avoid lazy initialization
		// exception
		Cart cart = target.getCart();
		session.setAttribute("cart", cart);
		logger.debug("Cart details retrieved from database: " + cart);

		// Remove from cart and update

		// Determine if cart has the product that is requested to be removed
		// Create a new CartItem object for searching purposes
		Optional<CartItem> optionalItem = cart.findMatchingCartItem(new CartItem(product, quantity));
		if (optionalItem.isEmpty()) {
			logger.info("Item not found in cart: " + product + " quantity: (" + quantity + ")");
			return "redirect:/product/dashboard";
		}

		// if cart has matching item, proceed to remove requested quantity
		CartItem matchingItem = optionalItem.get();
		logger.debug("Matching item with identical product found: " + matchingItem);

		// if requested quantity exceeds quantity in cart, then remove item entirely,
		// and remove from cartItem database
		if (quantity >= matchingItem.getProductQuantity()) {
			logger.debug("Requested quantity-to-remove (" + quantity + ") is greater than matching item quantity" + " ("
					+ matchingItem.getProductQuantity() + ")");
			cartService.removeFromCart(customer, product, quantity);
			logger.info("Item removed from cart: " + product + " quantity: (" + quantity + ")");

			// removing from database
			cartItemService.deleteCartItemFromDatabase(matchingItem);
			logger.debug("CartItem instance is removed from cart_item table: " + matchingItem);

		} else {
			// otherwise, just change quantity
			cartService.removeFromCart(customer, product, quantity);
			logger.info("Item removed: " + product + " quantity: (" + quantity + ")");
		}

		// Update total price and persist to avoid discrepancies from database
		cart.updateTotalPrice();
		cartService.updateCart(cart);
		logger.debug("Cart updated: " + cart);

		// Persist customer details to database
		target.setCart(cart);
		customerService.updateCustomer(target);
		logger.debug("Customer updated: " + customer);

		return "redirect:/product/dashboard";
	}

	/**
	 * This method is used to update the quantity of an item in the cart. This
	 * method is called when a customer presses the +/- buttons on the webpage,
	 * indicating to add or remove one unit of product
	 * 
	 * This method confirms if the customer wishes to add or remove one unit of the
	 * requested product, then calls the addToCart or removeFromCart methods
	 * accordingly
	 *
	 * @param customer  the customer information
	 * @param productID the product ID
	 * @param direction the direction of the update, either "plus" or "minus"
	 * @return the redirect to the product dashboard
	 * 
	 * @see #addToCart(Customer, int, int)
	 * @see #removeFromCart(Customer, int, int)
	 * 
	 */
	@PostMapping("/updateCartItemQuantity")
	public String updateCartItemQuantity(@SessionAttribute Customer customer, @RequestParam int productID,
			@RequestParam(value = "updateQuantity") String direction) {

		logger.debug("Customer requests to update quantity from pressing +/- button");

		// if product is not found, redirect to dashboard
		Optional<Product> optionalProduct = productService.findProductById(productID);
		if (optionalProduct.isEmpty()) {
			logger.info("Product not found, please check productID: " + productID);
			return "redirect:/product/dashboard";
		}

		// if customer is not found, redirect to dashboard
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			logger.info("Customer not found, please check customerID: " + customer.getCustomerID());
			return "redirect:/product/dashboard";
		}

		// if product and customer is found, proceed
		Customer target = optionalCustomer.get();
		Product product = optionalProduct.get();
		logger.debug("Customer details retrieved from database: " + target);
		logger.debug("Product found: " + product);

		// Get customer's cart and set as session attribute
		Cart cart = target.getCart();
		session.setAttribute("cart", cart);
		int quantity = 1;

		// Get direction
		if (direction.equals("plus")) {
			addToCart(customer, productID, quantity);
		} else {
			removeFromCart(customer, productID, quantity);
		}

		// Update total price and persist to avoid discrepancies from database
		cart.updateTotalPrice();

		// Save as customer
		customer.setCart(cart);
		customerService.updateCustomer(target);

		return "redirect:/product/dashboard";
	}

	/**
	 * Process cart checkout for the logged-in customer. Retrieves the customer's
	 * details from the session attribute, converts the cart items into an order,
	 * clears the cart, and updates the customer and cart information in the
	 * database. The new order is then persisted onto the database for future
	 * reference.
	 *
	 * @param customer The logged-in customer retrieved from session attribute.
	 * @return A redirection to the product dashboard page.
	 * 
	 * @see OrderService#createOrder(Customer)
	 */
	@PostMapping("/checkout")
	public String submitOrder(@SessionAttribute Customer customer) {
		logger.debug("Cart checkout requested for customer " + customer);

		// if customer is not found, redirect to login page
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			return "customerLogin";
		}

		// if customer is found, proceed to create order
		Customer currentCustomer = optionalCustomer.get();
		Order newOrder = orderService.createOrder(currentCustomer);

		// update product stock for items in order
		newOrder.getOrderedItems().forEach(item -> {
			Product productOrdered = item.getProduct();
			int quantityOrdered = item.getProductQuantity();
			Optional<Product> optionalProductInDatabase = productService.findProductById(productOrdered.getProductID());
			if (optionalProductInDatabase.isPresent()) {
				Product productInDatabase = optionalProductInDatabase.get();
				productInDatabase.setStock(productInDatabase.getStock() - quantityOrdered);
				productService.updateProduct(productInDatabase);
			}
		});

		// clear cart once order has been created to avoid mismanaged items
		Cart cart = currentCustomer.getCart();
		cartService.clearCart(cart);

		// persist onto database
		cartService.updateCart(cart);
		currentCustomer.setCart(cart);
		customerService.updateCustomer(currentCustomer);
		return "redirect:/product/dashboard";

	}

	/**
	 * Handles the POST request for refreshing the customer's cart. This method
	 * updates the customer's information and cart in the session and redirects to
	 * the product dashboard page.
	 *
	 * @param customer the currently logged-in customer retrieved from session
	 *                 attributes.
	 * @return link the redirection to the product dashboard page.
	 */
	@PostMapping("/refresh")
	public String refresh(@SessionAttribute Customer customer) {

		// if customer is not found, redirect to login page
		Optional<Customer> optionalCustomer = customerService.findCustomerByID(customer.getCustomerID());
		if (optionalCustomer.isEmpty()) {
			return "customerLogin";
		}

		// Retrieve the current customer and their cart from the session
		Customer currentCustomer = optionalCustomer.get();
		Cart currentCart = currentCustomer.getCart();

		// Update the customer and cart in the database
		customerService.updateCustomer(currentCustomer);
		cartService.updateCart(currentCart);

		// Update the session attributes with the updated customer and cart
		session.setAttribute("customer", currentCustomer);
		session.setAttribute("cart", currentCart);

		// Redirect to the product dashboard page
		return "redirect:/product/dashboard";
	}

}
