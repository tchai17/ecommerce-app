package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;

/**
 * This interface provides the necessary methods to interact with the database
 * for the CartItem entity.
 * 
 * The CartItem entity represents an item in a customer's shopping cart. It
 * contains information such as the product, the quantity, and the price.
 * 
 * @author - timothy.chai
 * 
 * @see CartItem
 */
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}