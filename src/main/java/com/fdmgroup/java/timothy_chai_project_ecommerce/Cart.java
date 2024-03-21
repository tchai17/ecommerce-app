package com.fdmgroup.java.timothy_chai_project_ecommerce;

import java.util.HashMap;
import java.util.Map;

class Cart {

	private int cartID;
	private Map<Product, Integer> items;
	private float totalPrice;
	
	public Cart() {
		items = new HashMap<>();
	}
	
	public int getCartID() {
		return cartID;
	}
	public Map<Product, Integer> getItems() {
		return items;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setCartID(int cartID) {
		this.cartID = cartID;
	}
	public void setItems(Map<Product, Integer> items) {
		this.items = items;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void addToCart(Product product, int quantity) {
		
		// check if product exists in db
		
		// if product exists in cart
		if ( items.containsKey(product) ){
			items.put(product, items.get(product) + quantity);
		}
		else {
			// if product does not exist in cart
			items.put(product, quantity);
	        totalPrice += product.getPrice() * quantity;
		}
		;
	}
	
	public void removeFromCart(Product product, int quantity) {
		
		// check if product exists in db
		
        // if product exists in cart
        if ( !items.containsKey(product) ){
        	return;
        }     
		
        if ( quantity >= items.get(product) ) {
        	// if quantity >= cart quantity
    		items.remove(product, quantity);
            totalPrice -= product.getPrice();
        } else {
        	// if quantity < cart quantity
            items.put(product, items.get(product) - quantity);
        }
		
        
        
    }
	
	public void checkout() {
		
	}
	
	
	
}
