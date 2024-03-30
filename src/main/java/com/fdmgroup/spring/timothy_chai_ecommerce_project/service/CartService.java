package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartRepository;

@Service
public class CartService {

	private CartRepository cartRepo;
	
	public CartService(CartRepository cartRepo) {
		this.cartRepo = cartRepo;
	}
	
	public Optional<CartItem> findCartItemInCart(Cart cart, Product product){
		return cart.findMatchingCartItem(new CartItem(product, 0));
	}
	
	public void updateCart(Cart cart) {
		cartRepo.save(cart);
	}
	
	/**
	 * Adds a new product to the customer's cart.
	 * 
	 * @param customer the customer who is adding the product to their cart
	 * @param product  the product that is being added to the cart
	 * @param quantity the quantity of the product that is being added to the cart
	 */
	public void addToCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		cart.addToCart(new CartItem(product, quantity));
		this.updateCart(cart);
	}

	/**
	 * Removes a product from the customer's cart.
	 * 
	 * @param customer the customer who is removing the product from their cart
	 * @param product  the product that is being removed from the cart
	 * @param quantity the quantity of the product that is being removed from the
	 *                 cart
	 */
	public void removeFromCart(Customer customer, Product product, int quantity) {
		Cart cart = customer.getCart();
		cart.removeFromCart(new CartItem(product, quantity));
		this.updateCart(cart);
	}

	/**
	 * Checks out the customer's cart.
	 * 
	 * @param customer the customer whose cart is being checked out
	 */
	public void checkoutCart(Customer customer) {
		customer.getCart().checkout();
		this.updateCart(customer.getCart());
	}
	
}
