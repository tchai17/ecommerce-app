package com.fdmgroup.java.timothy_chai_project_ecommerce;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CART_ITEM_ID")
	private int cartItemId;
	@Column(name = "PRODUCT_QUANTITY")
	private int productQuantity;
	@Column(name = "PRODUCT_SUBTOTAL")
	private double productSubtotal;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "CART_ID")
	private Cart cart;
	
	public CartItem(Product product, int productQuantity) {
		setProduct(product);
		setProductQuantity(productQuantity);
		calculateProductSubtotal();
	}

	public int getCartItemId() {
		return cartItemId;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public double getProductSubtotal() {
		return productSubtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public void calculateProductSubtotal() {
		this.productSubtotal = this.product.getPrice() * productQuantity;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	
	@Override
	public String toString() {
		return "CartItem [productQuantity=" + productQuantity + ", product=" + product + ", cart=" + cart + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cart, product);
	}

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

//	@Override
//	public int hashCode() {
//		return Objects.hash(cart, cartItemId, product);
//	}

	
	
	
	
	
}
