package com.fdmgroup.java.timothy_chai_project_ecommerce;

class Product {

	private int productID;
	private String productName;
	private int stock;
	private String imgURL;
	private float price;
	
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
	
	public float getPrice() {
        return price;
    }
	
    public void setPrice(float price) {
        this.price = price;
    }
	
	
}
