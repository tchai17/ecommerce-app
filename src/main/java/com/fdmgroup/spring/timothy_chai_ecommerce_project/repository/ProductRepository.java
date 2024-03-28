package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;


@Repository
public interface ProductRepository {
	
	public List<Product> findByProductName(String productName);	

}
