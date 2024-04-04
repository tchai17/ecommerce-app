package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;

/**
 * This interface is used to define the methods that will be used to interact
 * with the Customer table in the database.
 * 
 * @author - timothy.chai
 * 
 * @see Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * This method is used to retrieve a list of customers based on the username.
	 *
	 * @param username The username of the customer
	 * @return A list of customers that match the username
	 */
	public List<Customer> findByUsername(String username);

	/**
	 * This method is used to retrieve a list of customers based on the email.
	 *
	 * @param email The email of the customer
	 * @return A list of customers that match the email
	 */
	public List<Customer> findByEmail(String email);

	/**
	 * This method is used to retrieve a list of customers based on the address.
	 *
	 * @param address The address of the customer
	 * @return A list of customers that match the address
	 */
	public List<Customer> findByAddress(String address);

	/**
	 * This method is used to retrieve a list of customers based on the full name.
	 *
	 * @param fullName The full name of the customer
	 * @return A list of customers that match the full name
	 */
	public List<Customer> findByFullName(String fullName);

}
