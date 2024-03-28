package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	public List<Product> findByProductName(String productName);


}
