package com.fdmgroup.java.timothy_chai_project_ecommerce;

class User {

	private int userID;
	private String username;
	private String password;
	private String email;
	private Cart cart;
	
	public User() {
		cart = new Cart();
	}
	
	public User(String username, String password, String email) {
		setUsername(username);
		setPassword(password);
		setEmail(email);
		
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
	
	
}
