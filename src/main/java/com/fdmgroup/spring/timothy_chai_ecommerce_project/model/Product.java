/** Holds all classes relevant for running the e-commerce application.
 * 
 */
package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Represents products in the online shop
 * 
 * @author - timothy.chai
 * 
 *         The Product class encapsulates all product information like the name,
 *         available stock, price, and a URL to an image of the product
 *         Customers can view product information using respective getters, and
 *         add products to the cart through the use of the CartItem class
 * 
 * @see CartItem
 */
@Entity
public class Product {

	/**
	 * ID which uniquely identifies this product productID is automatically assigned
	 * to the product upon persistence
	 * 
	 * @see #getProductID()
	 * @see #setProductID(int)
	 */
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private int productID;

	/**
	 * Name of product
	 * 
	 * @see #getProductName()
	 * @see #setProductName(String)
	 */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/**
	 * Amount of stock for this product
	 * 
	 * @see #getStock()
	 * @see #setStock(int)
	 */
	@Column(name = "STOCK")
	private int stock;

	/**
	 * URL link to an image for this product
	 *
	 * @see #getImgURL()
	 * @see #setImgURL(String)
	 */
	@Column(name = "IMAGE_URL")
	private String imgURL;

	/**
	 * Price of product
	 * 
	 * @see #getPrice()
	 * @see #setPrice(double)
	 */
	@Column(name = "PRICE")
	private double price;

	@Column(name = "CATEGORY")
	private String category;

	/**
	 * Default no-args constructor
	 */
	public Product() {

	}

	/**
	 * Constructor for adding a new product to the application
	 * 
	 * @param productName Name of the product
	 * @param stock       Available stock
	 * @param imgURL      URL link to an image of the product
	 * @param price       Price of the product
	 */
	public Product(String productName, int stock, String imgURL, double price) {
		setProductName(productName);
		setStock(stock);
		setImgURL(imgURL);
		setPrice(price);
	}

	/**
	 * Getter for productID
	 * 
	 * @return productID ID of product used to uniquely identify this product
	 */
	public int getProductID() {
		return productID;
	}

	/**
	 * Getter for product name
	 * 
	 * @return product Name of product
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Getter for current available stock of product
	 * 
	 * @return stock Amount of stock inventory available for order
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * Getter for URL link to image of product
	 * 
	 * @return imgURL URL link to image of product
	 */
	public String getImgURL() {
		return imgURL;
	}

	/**
	 * Getter for product price
	 * 
	 * @return price Price of product
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Setter for productID
	 * 
	 * @param productID New product ID to set for product
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}

	/**
	 * Setter for product name
	 * 
	 * @param productName New product name to set for product
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Setter for stock of product
	 * 
	 * @param stock Updated inventory for this product
	 */
	public void setStock(int stock) {
		if (stock <= 0) {
			System.out.println("Stock must be positive!");
			return;
		} else {
			this.stock = stock;
		}

	}

	/**
	 * Setter for image URL link of product
	 * 
	 * @param imgURL New URL link for the image of this product
	 */
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	/**
	 * Setter for product price
	 * 
	 * @param price Updated price of product
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Returns the category of the product.
	 *
	 * @return the category of the product
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category of the product.
	 *
	 * @param category the new category of the product
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Overriden toString method for printing Product onto console
	 */
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", price=" + price + "]";
	}

	/**
	 * Overriden .equals method for tests
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(imgURL, other.imgURL)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& productID == other.productID && Objects.equals(productName, other.productName)
				&& stock == other.stock;
	}

}
