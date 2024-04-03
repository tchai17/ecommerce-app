package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.OrderService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@Transactional(readOnly = false)
public class CartController {

	private Logger logger = LogManager.getLogger(CartController.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private HttpSession session;

	@RequestMapping("/")
	public String index() {
		return "index";
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
			session.setAttribute("cart", cart);

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
			session.setAttribute("cart", cart);
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
			session.setAttribute("cart", cart);

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
	 * details from the session attribute, converts the cart items into an order,
	 * clears the cart, and updates the customer and cart information in the
	 * database. The new order is then persisted onto the database for future
	 * reference.
	 *
	 * @param customer The logged-in customer retrieved from session attribute.
	 * @return A redirection to the product dashboard page.
	 */
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

	@PostMapping("/refresh")
	public String refresh(@SessionAttribute Customer customer) {
		Customer currentCustomer = customerService.findCustomerByID(customer.getCustomerID()).get();
		Cart currentCart = currentCustomer.getCart();

		customerService.updateCustomer(currentCustomer);
		cartService.updateCart(currentCart);

		session.setAttribute("customer", currentCustomer);
		session.setAttribute("cart", currentCart);

		return "redirect:/product/dashboard";
	}

}
