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


@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CART_ID")
	private int cartID;
	@Column(name = "TOTAL_PRICE")
	private double totalPrice;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
	private Set<CartItem> items;
	
	public Cart() {
		items = new HashSet<>();
	}
	
	public int getCartID() {
		return cartID;
	}
	public Set<CartItem> getItems() {
		return items;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setCartID(int cartID) {
		this.cartID = cartID;
	}
	public void setItems(Set<CartItem> items) {
		this.items = items;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
	
	@Override
	public String toString() {
		return "Cart [cartID=" + cartID + "]";
	}

	public void addToCart(CartItem item) {
			
		if ( cartHasProduct(item) ) {
			items.forEach( cartItem -> {
				if ( cartItem.getProduct().equals(item.getProduct()) ) {
                    cartItem.setProductQuantity(cartItem.getProductQuantity() + item.getProductQuantity());
                }
			});
		}
		else {
			items.add(item);
		}

		
		;
	}
	
	public void removeFromCart(CartItem item) {
		
		CartItem targetItem = new CartItem( new Product(), 0 );
		if ( cartHasProduct(item) ) {
			Optional<CartItem> target = findMatchingCartItem(item);
			if ( target.isPresent() ) {
				targetItem = target.get();
			}
		}
		else {
			return;
		}
		if ( targetItem.getProductQuantity() > item.getProductQuantity() ) {
			targetItem.setProductQuantity(targetItem.getProductQuantity() - item.getProductQuantity());
		
		} else {
			items.remove(targetItem);
		}   
        
    }
	
	private boolean cartHasProduct ( CartItem item ) {
		List<Product> products = new ArrayList<>();
		
		items.forEach( cartItem -> products.add(cartItem.getProduct()) );
		
		if ( products.contains( item.getProduct() ) ) {
			return true;
		}
		return false;
	}
	
	private Optional<CartItem> findMatchingCartItem ( CartItem item ) {
		
		for ( CartItem cartItem : items ) {
			if ( cartItem.getProduct().equals(item.getProduct()) ) {
                return Optional.of(cartItem);
            }
		}
		return Optional.empty();
		
	}
	
	public void checkout() {
		items.clear();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cartID, items, totalPrice);
	}

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
