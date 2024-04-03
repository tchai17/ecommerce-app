package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Order;

/**
 * This interface is used to interact with the database and perform operations
 * on the orders table.
 * 
 * @author timothy.chai
 * 
 * @see Order
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
