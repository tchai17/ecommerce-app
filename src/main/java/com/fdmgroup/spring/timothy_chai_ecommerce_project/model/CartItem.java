/** Holds all classes relevant for running the e-commerce application.
 * 
 */
package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a line item in the shopping cart
 * 
 * 
 * 
 * The CartItem class encapsulates ordering information, holding reference to
 * the Product class and other relevant information like the product quantity
 * and subtotal. It is used primarily when customers wish to add products to
 * their cart When customers wish to add a product to the cart, a new CartItem
 * object is created encapsulating the product and the quantity.
 * 
 * When a Cart is checked-out, CartItems are linked to the Order that is created
 * based on the Cart
 * 
 * @author - timothy.chai
 * 
 * @see Product
 * @see Cart
 */
@Entity
public class CartItem {

	/**
	 * ID that uniquely identifies this CartItem Automatically assigned when
	 * persisted
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CART_ITEM_ID")
	private int cartItemId;

	/**
	 * Product quantity for the product that is associated with this CartItem
	 */
	@Column(name = "PRODUCT_QUANTITY")
	private int productQuantity;

	/**
	 * Subtotal cost for this CartItem
	 */
	@Column(name = "PRODUCT_SUBTOTAL")
	private double productSubtotal;

	/**
	 * Product reference for this CartItem
	 * 
	 * @see Product
	 */
	@ManyToOne
	@JoinColumn(name = "FK_PRODUCT_ID")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "FK_ORDER_ID")
	private Order order;

	/**
	 * Default no-args constructor
	 */
	public CartItem() {
	}

	/**
	 * Custom constructor used for specifying the product reference and the product
	 * quantity
	 * 
	 * @param product         Product that is intended to be added to cart
	 * @param productQuantity The quantity of the specified product that the
	 *                        customer wishes to order
	 * @see Product
	 */
	public CartItem(Product product, int productQuantity) {
		setProduct(product);
		setProductQuantity(productQuantity);
		calculateProductSubtotal();
	}

	/**
	 * Getter for the ID for this CartItem
	 * 
	 * @return cartItemId ID for this CartItem
	 */
	public int getCartItemId() {
		return cartItemId;
	}

	/**
	 * Getter for quantity of the product for this CartItem
	 * 
	 * @return productQuantity Quantity of specified Product
	 */
	public int getProductQuantity() {
		return productQuantity;
	}

	/**
	 * Getter for the subtotal of the product(s) for this CartItem' Subtotal is
	 * calculated by taking the Product price multiplied by the ordered quantity
	 * 
	 * @return productSubtotal Subtotal for this Product order
	 */
	public double getProductSubtotal() {
		return productSubtotal;
	}

	/**
	 * Getter for the Product reference of this CartItem
	 * 
	 * @return Product Product which customer wishes to order
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Setter for the CartItemID of this CartItem
	 * 
	 * @param cartItemId new ID to set for this CartItem
	 */
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}

	/**
	 * Setter for productQuantity of this CartItem
	 * 
	 * @param productQuantity new quantity to set for this CartItem
	 */
	public void setProductQuantity(int productQuantity) {

		if (productQuantity > this.product.getStock()) {
			System.out.println("The quantity of the product you are trying to order "
					+ "is greater than the stock of the product. Quantity is changed to available stock.");
			this.productQuantity = this.product.getStock();
		} else {
			this.productQuantity = productQuantity;
		}

	}

	/**
	 * Calculates productSubtotal of this CartItem, based on the Product and its
	 * quantity, and sets it as the subtotal
	 * 
	 */
	public void calculateProductSubtotal() {
		this.productSubtotal = this.product.getPrice() * productQuantity;
	}

	/**
	 * Setter for the Product reference for this CartItem
	 * 
	 * @param product product that customer wishes to purchase
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * Get the order that this cart item belongs to.
	 * 
	 * @return the order that this cart item belongs to
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * Set the order that this cart item belongs to.
	 * 
	 * @param order the order that this cart item belongs to
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * Sets the product subtotal.
	 *
	 * @param productSubtotal the product subtotal
	 */
	public void setProductSubtotal(double productSubtotal) {
		this.productSubtotal = productSubtotal;
	}

	/**
	 * Overriden toString method for printing CartItem objects to console
	 */
	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", productQuantity=" + productQuantity + ", productSubtotal="
				+ productSubtotal + ", product=" + product + "]";
	}

	/**
	 * Overriden .equals method for testing
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		return Objects.equals(order, other.order) && Objects.equals(product, other.product)
				&& productQuantity == other.productQuantity
				&& Double.doubleToLongBits(productSubtotal) == Double.doubleToLongBits(other.productSubtotal);
	}

}
