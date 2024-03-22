package com.fdmgroup.java.timothy_chai_project_ecommerce.app;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private int productID;
	@Column(name = "PRODUCT_NAME")
	private String productName;
	@Column(name = "STOCK")
	private int stock;
	@Column(name = "IMAGE_URL")
	private String imgURL;
	@Column(name = "PRICE")
	private double price;

	public Product() {

	}

	public Product(String productName, int stock, String imgURL, double price) {
		setProductName(productName);
		setStock(stock);
		setImgURL(imgURL);
		setPrice(price);
	}

	public int getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getStock() {
		return stock;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", price=" + price + "]";
	}

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
