package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.springframework.lang.NonNull;
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
		return cart.findMatchingCartItem(new CartItem(product, 0));
	}

	/**
	 * Updates the specified cart in the database.
	 * 
	 * @param cart the cart to update
	 */
	public void updateCart(@NonNull Cart cart) {
		cartRepo.save(cart);
	}

	/**
	 * Adds the specified product to the specified customer's cart.
	 * 
	 * @param customer the customer adding the product to their cart
	 * @param product  the product being added to the cart
	 * @param quantity the quantity of the product being added to the cart
	 */
	public void addToCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		CartItem newItem = new CartItem(product, quantity);
		newItem.setCart(cart);
		cart.addToCart(newItem);
		cart.updateTotalPrice();
		updateCart(cart);
	}

	/**
	 * Removes the specified product from the specified customer's cart.
	 * 
	 * @param customer the customer removing the product from their cart
	 * @param product  the product being removed from the cart
	 * @param quantity the quantity of the product being removed from the cart
	 */
	public void removeFromCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		CartItem newItem = new CartItem(product, quantity);
//		newItem.setCart(cart);
		cart.removeFromCart(newItem);
		cart.updateTotalPrice();
		updateCart(cart);
	}

	/**
	 * Checks out the specified customer's cart.
	 * 
	 * @param customer the customer whose cart is being checked out
	 */
	public void checkoutCart(Cart cart) {
		cart.checkout();
		updateCart(cart);
	}

}