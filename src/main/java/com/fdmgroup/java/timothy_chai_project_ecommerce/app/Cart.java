package com.fdmgroup.java.timothy_chai_project_ecommerce.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/** Represents a shopping cart for the customer
 * @author - timothy.chai
 * 
 * The Cart class holds a set of CartItem objects which refer to Product classes. The Cart class also holds 
 * the relevant methods for adding and removing CartItem objects to and from the Cart.
 * 
 * @see Customer
 * @see CartItem
 * @see #addToCart(CartItem)
 * @see #removeFromCart(CartItem)
 * 
 */
@Entity
public class Cart {

	/**
	 * Assigned ID for this Cart object
	 *
	 * @see #getCartID()
	 * @see #setCartID(int)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CART_ID")
	private int cartID;
	
	/**
	 * Total price of all items in the cart
	 * 
	 * @see #getTotalPrice()
	 * @see #setTotalPrice(double)
	 */
	@Column(name = "TOTAL_PRICE")
	private double totalPrice;

	/**
	 * Contains the set of CartItem objects in the cart
	 * 
	 * @see CartItem
	 * @see #getItems()
	 * @see #setItems(Set)
	 */
	@OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
	private Set<CartItem> items;

	
	/**
	 * Default no-args constructor for the Cart class
	 * Initializes the items set for holding CartItem objects
	 * @see #items
	 */
	public Cart() {
		items = new HashSet<>();
	}

	/**
	 * Getter for cartID, which is automatically assigned when Cart is persisted
	 * @return cartID CartID assigned to this cart
	 */
	public int getCartID() {
		return cartID;
	}

	/**
	 * Getter for items, the set of CartItem objects specifying the product and the relevant quantities
	 * @return items Set of CartItem objects specifying the products and quantities
	 * @see CartItem
	 */
	public Set<CartItem> getItems() {
		return items;
	}

	/**
	 * Getter for total price of cart
	 * @return totalPrice Total price of all items in cart
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Setter for cartID 
	 * @param cartID Integer to set cartID to
	 */
	public void setCartID(int cartID) {
		this.cartID = cartID;
	}

	/**
     * Setter for items
     * @param items Set of CartItem objects specifying the products and quantities
     * @see CartItem
     */
	public void setItems(Set<CartItem> items) {
		this.items = items;
	}

	/**
     * Setter for total price of cart
     * @param totalPrice Total price of all items in cart
     */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * Overriden toString method for printing Cart objects with ID only
	 */
	@Override
	public String toString() {
		return "Cart [cartID=" + cartID + "]";
	}

	/**
	 * Adds the input CartItem to the items set in the cart.
	 * If the cart already contains another CartItem with the same ProductID, then its quantity will be increased instead
	 * @param item Specified CartItem to add
	 * @see CartItem
	 */
	public void addToCart(CartItem item) {

		// Check if cart has item which has a product which matches the input cartItem
		if (cartHasProduct(item)) {
			items.forEach(cartItem -> {
				// if cart has product, add to quantity
				if (cartItem.getProduct().equals(item.getProduct())) {
					cartItem.setProductQuantity(cartItem.getProductQuantity() + item.getProductQuantity());
				}
			});
		} else {
			// If cart does not have the incoming product, add item to cart
			items.add(item);
		}
	}

	/**
	 * Removes specified CartItem from cart. If the CartItem specified has a larger quantity than the quantity in the cart, 
	 * the target CartItem will be removed. Otherwise, the product quantity of the CartItem will be subtracted to reflect the 
	 * correct quantity
	 * @param item CartItem which specifies the product and quantity
	 * @see CartItem
	 */
	public void removeFromCart(CartItem item) {

		CartItem targetItem = new CartItem(new Product(), 0);
		if (cartHasProduct(item)) {
			Optional<CartItem> target = findMatchingCartItem(item);
			if (target.isPresent()) {
				targetItem = target.get();
			}
		} else {
			return;
		}

		if (targetItem.getProductQuantity() > item.getProductQuantity()) {
			targetItem.setProductQuantity(targetItem.getProductQuantity() - item.getProductQuantity());

		} else {
			items.remove(targetItem);
		}

	}

	/**
	 * Helper method used by addToCart and removeFromCart methods to determine if the cart has the product matching the input CartItem
	 * @see #addToCart(CartItem)
	 * @see #removeFromCart(CartItem)
	 * @param item Input CartItem to check against
	 * @return result Returns true if cart contains a CartItem with a matching product ID 
	 */
	private boolean cartHasProduct(CartItem item) {
		List<Product> products = new ArrayList<>();

		items.forEach(cartItem -> products.add(cartItem.getProduct()));

		if (products.contains(item.getProduct())) {
			return true;
		}
		return false;
	}

	/**
	 * Helper method used by removeFromCart to find the CartItem with the matching product
	 * @see #removeFromCart(CartItem)
	 * @see #cartHasProduct(CartItem)
	 * @param item Input CartItem to check against
	 * @return result Returns an Optional wrapping the target CartItem
	 */
	private Optional<CartItem> findMatchingCartItem(CartItem item) {

		for (CartItem cartItem : items) {
			if (cartItem.getProduct().equals(item.getProduct())) {
				return Optional.of(cartItem);
			}
		}
		return Optional.empty();

	}

	/**
	 * Checks out all items in the cart
	 * Implemented in later versions
	 */
	public void checkout() {
		items.clear();
	}

	/**
	 * Overriden .equals() method for testing purposes
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		return cartID == other.cartID && Objects.equals(items, other.items)
				&& Double.doubleToLongBits(totalPrice) == Double.doubleToLongBits(other.totalPrice);
	}

}
