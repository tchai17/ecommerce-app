package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
