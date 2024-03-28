package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;



@Service
public class ProductService {

	private ProductRepository productRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepository = productRepo;
    
	}
	
	public void saveProduct(Product product) {
		
		productRepository.save(product);
	}
	
	public Optional<Product> findProductById(int productId) {
		
		Optional<Product> product = productRepository.findById(productId);
		if ( product.isPresent() ) {
			return product;
			
		} else {
			return Optional.empty();
		}
	}
	
	public List<Product> findProductByProductName(String productName) {
        return productRepository.findByProductName(productName);
	}
	
	public List<Product> returnAllProducts(){
		return productRepository.findAll();
	}
	
}
