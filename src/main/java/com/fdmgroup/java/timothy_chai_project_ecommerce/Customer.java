package com.fdmgroup.java.timothy_chai_project_ecommerce;


class Customer {

	
	// Attributes
	private int userID;
	private String username;
	private String password;
	private String email;
	private String address;
	private String fullName;
	private String cardNumber;
	
	private Cart cart;
	
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

	public int getUserID() {
		return userID;
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

	public void setUserID(int userID) {
		this.userID = userID;
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
		
	
}
