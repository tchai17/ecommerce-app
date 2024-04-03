/** Holds all classes relevant for running the e-commerce application.
 * 
 */
package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/**
 * Represents a Customer
 * 
 * @author - timothy.chai
 * 
 *         The Customer class is intended to represent customers in this
 *         application. Customers must sign up for an account with their
 *         relevant details: username, password, email address, delivery
 *         address, full name, and credit card information. Upon creating an
 *         account and persisting onto the database, the Customer instance is
 *         automatically assigned an ID, and will have a shopping Cart object
 *         which the customer can use to purchase products.
 * 
 *         The Customer class has mainly getters and setters for each attribute,
 *         except for the cart attribute, which only has a getter. The Customer
 *         class also has updateDetails which allows existing customers to
 *         update any of the attributes, except for the cart attribute and the
 *         customerID, which cannot be changed after registration.
 * 
 * @see Cart
 * @see #updateDetails(Customer)
 * 
 */

@Entity
public class Customer {

	/**
	 * The ID assigned to the customer
	 * 
	 * @see #getCustomerID()
	 * @see #setCustomerID(int)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CUSTOMER_ID")
	private int customerID;

	/**
	 * The current username of the customer
	 * 
	 * @see #getUsername()
	 * @see #setUsername(String)
	 */
	@Column(name = "USERNAME")
	private String username;

	/**
	 * The password set for this customer account
	 * 
	 * @see #getPassword()
	 * @see #setPassword(String)
	 */
	@Column(name = "PASSWORD")
	private String password;

	/**
	 * The email address provided for this customer account
	 * 
	 * @see #getEmail()
	 * @see #setEmail(String)
	 */
	@Column(name = "EMAIL")
	private String email;
	/**
	 * The mailing address provided for this customer account
	 * 
	 * @see #getAddress()
	 * @see #setAddress(String)
	 */
	@Column(name = "ADDRESS")
	private String address;
	/**
	 * The full name provided for shipping purposes
	 * 
	 * @see #getFullName()
	 * @see #setFullName(String)
	 */
	@Column(name = "FULL_NAME")
	private String fullName;
	/**
	 * The saved credit card number provided for payment
	 * 
	 * @see #getCardNumber()
	 * @see #setCardNumber(String)
	 */
	@Column(name = "CARD_NUMBER")
	private String cardNumber;

	/**
	 * The shopping Cart object assigned to the user.
	 * 
	 * The Cart object holds the set of items that the user wishes to buy, and the
	 * relevant methods for adding and removing items
	 * 
	 * @see Cart
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_CART_ID")
	private Cart cart;

	/**
	 * The products that are 'liked' by this customer
	 * 
	 * The likes field in the Customer class represents the set of products that the
	 * customer likes.
	 * 
	 * 
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LIKES", joinColumns = @JoinColumn(name = "FK_CUSTOMER_ID"), inverseJoinColumns = @JoinColumn(name = "FK_PRODUCT_ID"))
	private Set<Product> likes;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Order> orders;

	/**
	 * Default no-args constructor for the Customer class
	 */
	public Customer() {

	}

	/**
	 * Custom constructor for the Customer class, allowing new customers to specify
	 * relevant details
	 * 
	 * @param username   Customer's account username
	 * @param password   Customer's account password
	 * @param email      Customer's email address'
	 * @param address    Customer's delivery address for orders
	 * @param fullName   Customer's full name for shipping purposes'
	 * @param cardNumber Customer's credit card information
	 */
	public Customer(String username, String password, String email, String address, String fullName,
			String cardNumber) {
		this.cart = new Cart();
		this.cart.setCustomer(this);
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setAddress(address);
		setFullName(fullName);
		setCardNumber(cardNumber);
		likes = new HashSet<>();
		orders = new ArrayList<>();
	}

	/**
	 * Getter for customerID
	 * 
	 * @return customerID Returns the auto-generated ID assigned by the system
	 */
	public int getCustomerID() {
		return customerID;
	}

	/**
	 * Getter for customer username
	 * 
	 * @return username Returns the current username of the customer's account
	 * 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Getter for account password
	 * 
	 * @return password Returns the current password of the customer's account
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Getter for email address
	 * 
	 * @return email Returns the current email address of the customer's account
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Getter for address
	 * 
	 * @return address Returns the current address of the customer's account
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Getter for customer full name
	 * 
	 * @return fullName Returns the full name of the customer as listed in the
	 *         system
	 * 
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Getter for credit card number
	 * 
	 * @return cardNumber Returns the card number registered in the system
	 * 
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Getter for shopping cart This method is typically called by the application,
	 * not by the user
	 * 
	 * @return cart Returns the Cart object associated with the account
	 * 
	 * @see Cart
	 */
	public Cart getCart() {
		return cart;
	}

	public Set<Product> getLikes() {
		return likes;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setCart(Cart cart) {
		cart.setCustomer(this);
		this.cart = cart;
	}

	/**
	 * Setter for customer ID
	 * 
	 * @param customerID Specifies the new ID to be used for this customer
	 */
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	/**
	 * Setter for customer username
	 * 
	 * @param username Specifies the new username to be used for this account
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Setter for customer password
	 * 
	 * @param password Specifies the new password to be used for this account
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Setter for customer email address
	 * 
	 * @param email Specifies the new email address to be used for this account
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Setter for customer's address
	 * 
	 * @param address Specifies the new address to be used for this account
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Setter for customer full name for shipping purposes
	 * 
	 * @param fullName Specifies the new full name to be used for this account
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Setter for new credit card number used for payment
	 * 
	 * @param cardNumber Specifies the new credit card number to be used for payment
	 */
	public void setCardNumber(String cardNumber) {

		if (isNumber(cardNumber) && cardNumber.length() == 16) {
			this.cardNumber = cardNumber;
		} else {
			System.out.println("Invalid cardNumber");
		}

	}

	public void setLikes(Set<Product> likes) {
		this.likes = likes;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * Updates customer details based on the input Customer object
	 * 
	 * @param customer New Customer object used to encapsulate all customer details
	 */
	public void updateDetails(Customer customer) {
		setUsername(customer.getUsername());
		setPassword(customer.getPassword());
		setEmail(customer.getEmail());
		setAddress(customer.getAddress());
		setFullName(customer.getFullName());
		setCardNumber(customer.getCardNumber());
		setLikes(customer.getLikes());

	}

	public void addToLikes(Product product) {
		likes.add(product);
	}

	public void removeFromLikes(Product product) {
		likes.remove(product);
	}

	/**
	 * Overriden toString method for printing Customer objects onto console
	 */
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", username=" + username + ", fullName=" + fullName + "]";
	}

	/**
	 * @return result Returns true if the input Customer object is equal to this
	 *         Customer object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(address, other.address) && Objects.equals(cardNumber, other.cardNumber)
				&& Objects.equals(cart, other.cart) && customerID == other.customerID
				&& Objects.equals(email, other.email) && Objects.equals(fullName, other.fullName)
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	private boolean isNumber(String stringNumber) {
		try {
			Long.parseLong(stringNumber);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;

	}

}
