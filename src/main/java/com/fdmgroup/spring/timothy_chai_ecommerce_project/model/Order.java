package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
	private int orderID;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_CUSTOMER_ID")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<CartItem> orderedItems;

	@Column(name = "ORDER_DATE")
	private Date orderDate;

	public Order() {

	}

	public Order(Cart cart, Customer customer) {
		orderedItems = new ArrayList<>();
		/*
		 * Creates a deep copy of all items in the cart Doing this avoids issues when
		 * cart is cleared or items are removed from the database
		 */
		cart.getItems().forEach(item -> {
			CartItem newCartItem = new CartItem();
			newCartItem.setProduct(item.getProduct());
			newCartItem.setProductQuantity(item.getProductQuantity());
			newCartItem.setProductSubtotal(item.getProductSubtotal());
			newCartItem.setOrder(this); // Set order reference for the new cart item
			orderedItems.add(newCartItem); // Persist new cart item
		});
		orderDate = new Date(); // current date and time
		setCustomer(customer);
	}

	/**
	 * Returns the order ID.
	 * 
	 * @return the order ID
	 */
	public int getOrderID() {
		return orderID;
	}

	/**
	 * Sets the order ID.
	 * 
	 * @param orderID the order ID to set
	 */
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	/**
	 * Returns the ordered items.
	 * 
	 * @return the ordered items
	 */
	public List<CartItem> getOrderedItems() {
		return orderedItems;
	}

	/**
	 * Sets the ordered items.
	 * 
	 * @param orderedItems the ordered items to set
	 */
	public void setOrderedItems(List<CartItem> orderedItems) {
		this.orderedItems = orderedItems;
	}

	/**
	 * Returns the customer.
	 * 
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer.
	 * 
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Returns the order date.
	 * 
	 * @return the order date
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * Sets the order date.
	 * 
	 * @param orderDate the order date to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(customer, other.customer) && Objects.equals(orderDate, other.orderDate)
				&& orderID == other.orderID && Objects.equals(orderedItems, other.orderedItems);
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", customer=" + customer + ", orderedItems=" + orderedItems
				+ ", orderDate=" + orderDate + "]";
	}

}
