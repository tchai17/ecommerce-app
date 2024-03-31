/** Holds all classes relevant for running the e-commerce application.
 * 
 */
package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * @author - timothy.chai
 * 
 *         The CartItem class encapsulates ordering information, holding
 *         reference to the Product class and other relevant information like
 *         the product quantity and subtotal. It is used primarily when
 *         customers wish to add products to their cart When customers wish to
 *         add a product to the cart, a new CartItem object is created
 *         encapsulating the product and the quantity, before it is added to the
 *         Cart
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

	/**
	 * Cart which this CartItem is associated with
	 * 
	 * @see Cart
	 */
	@ManyToOne
	@JoinColumn(name = "FK_CART_ID")
	private Cart cart;

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
		
		if ( productQuantity > this.product.getStock() ) {
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
		BigDecimal subtotal = new BigDecimal(this.product.getPrice() * productQuantity).setScale(2, RoundingMode.DOWN);
		this.productSubtotal = subtotal.doubleValue();
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
	 * Setter for the cart reference for this CartItem
	 * 
	 * @param cart cart which CartItem is associated with
	 */
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	/**
	 * Overriden toString method for printing CartItem objects to console
	 */
	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", productQuantity=" + productQuantity + ", productSubtotal="
				+ productSubtotal + ", product=" + product + ", cartID=" + cart.getCartID() + "]";
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
		return Objects.equals(cart, other.cart) && Objects.equals(product, other.product);
	}

}
