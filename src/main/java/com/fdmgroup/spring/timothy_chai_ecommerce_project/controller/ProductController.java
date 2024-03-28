package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
    public String index() {
        return "index";
    }
	
	@GetMapping("/add-product")
	public String addProduct() {
        return "addProduct";
    }
	
	@PostMapping("add-product")
	public String processRegistration(HttpServletRequest request) {
    	System.out.println("Initiate registration for new product");
    	
    	// Get parameters
    	String productName = request.getParameter("productName");
    	String stockString = request.getParameter("stock");
    	int stock = Integer.parseInt(stockString);
    	String imgURL = request.getParameter("imgURL");
    	String priceString = request.getParameter("price");
    	double price = Double.parseDouble(priceString);
    	
    	Product product = new Product(productName, stock, imgURL, price);
    	System.out.println(product);
    	
    	productService.saveProduct(product);
    	System.out.println("Product saved successfully");
    	
    	return "complete";
    
	}
}
