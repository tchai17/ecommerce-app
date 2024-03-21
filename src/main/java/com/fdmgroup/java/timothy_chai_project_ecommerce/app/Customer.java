package com.fdmgroup.java.timothy_chai_project_ecommerce.app;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Customer {

	
	// Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CUSTOMER_ID")
	private int customerID;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "FULL_NAME")
	private String fullName;
	@Column(name = "CARD_NUMBER")
	private String cardNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_CART_ID")
	private Cart cart;
	
	// Default no-args constructor should instantiate cart object
	public Customer() {
		cart = new Cart();
	}
	
	public Customer(String username, String password, String email, String address, String fullName, String cardNumber) {
		this();
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setAddress(address);
		setFullName(fullName);
		setCardNumber(cardNumber);
		
	}

	public int getCustomerID() {
		return customerID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	public String getAddress() {
		return address;
	}

	public String getFullName() {
		return fullName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setUserID(int customerID) {
		this.customerID = customerID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void updateDetails(Customer customer) {
		setUsername(customer.getUsername());
		setPassword(customer.getPassword());
		setEmail(customer.getEmail());
		setAddress(customer.getAddress());
		setFullName(customer.getFullName());
		setCardNumber(customer.getCardNumber());
		
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", username=" + username + ", fullName=" + fullName + "]";
	}
		
	
}
