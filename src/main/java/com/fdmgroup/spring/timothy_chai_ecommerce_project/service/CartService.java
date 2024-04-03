package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartRepository;

/**
 * This class provides services for managing a customer's shopping cart.
 */
@Service
public class CartService {

	private Logger logger = LogManager.getLogger(CartService.class);

	@Autowired
	private CartRepository cartRepo;

	/**
	 * Constructs a new CartService with the specified CartRepository.
	 * 
	 * @param cartRepo the CartRepository to use for persisting cart data
	 */
	public CartService(CartRepository cartRepo) {
		this.cartRepo = cartRepo;
	}

	/**
	 * Finds a CartItem in the specified cart that matches the specified Product.
	 * 
	 * @param cart    the cart to search
	 * @param product the product to match
	 * @return an Optional containing the matching CartItem, or an empty Optional if
	 *         no match is found
	 */
	public Optional<CartItem> findCartItemInCart(Cart cart, Product product) {
		logger.debug("findCartItemInCart is called");
		return cart.findMatchingCartItem(new CartItem(product, 0));
	}

	/**
	 * Updates the specified cart in the database.
	 * 
	 * @param cart the cart to update
	 */
	public void updateCart(Cart cart) {
		logger.debug("updateCart is called for cart: " + cart);
		Cart targetCart = cartRepo.findById(cart.getCartID()).get();
		logger.debug("Cart with matching cartID is found, saving items");

		targetCart.setItems(cart.getItems());

		cartRepo.save(targetCart);
		logger.info("Updated cart successfully");
	}

	/**
	 * Adds the specified product to the specified customer's cart.
	 * 
	 * @param customer the customer adding the product to their cart
	 * @param product  the product being added to the cart
	 * @param quantity the quantity of the product being added to the cart
	 */
	public void addToCart(Customer customer, Product product, int quantity) {
		logger.debug(
				"addToCart is called for customer: " + customer + " product: " + product + " quantity: " + quantity);

		Cart cart = customer.getCart();
		logger.debug("Cart of target customer retrieved");
		CartItem newItem = new CartItem(product, quantity);
		cart.addToCart(newItem);
		logger.debug("New CartItem is added to cart");
		cart.updateTotalPrice();
		updateCart(cart);
		logger.info("CartItem added successfully and cart database updated");
	}

	/**
	 * Removes the specified product from the specified customer's cart.
	 * 
	 * @param customer the customer removing the product from their cart
	 * @param product  the product being removed from the cart
	 * @param quantity the quantity of the product being removed from the cart
	 */
	public void removeFromCart(Customer customer, Product product, int quantity) {
		logger.debug("removeFromCart is called for customer: " + customer + " product: " + product + " quantity: "
				+ quantity);
		Cart cart = customer.getCart();
		logger.debug("Cart of target customer retrieved");
		CartItem newItem = new CartItem(product, quantity);
		cart.removeFromCart(newItem);
		logger.debug("CartItem is removed from cart: product - " + product + " quantity - " + quantity);
		cart.updateTotalPrice();
		updateCart(cart);
		logger.info("CartItem removed successfully and cart database updated");
	}

	/**
	 * Checks out the specified customer's cart.
	 * 
	 * @param customer the customer whose cart is being checked out
	 */
	public void clearCart(Cart cart) {
		logger.debug("clearCart is called for cart: " + cart);
		cart.clearCart();
		updateCart(cart);
		logger.info("clearCart is completed");
	}

}