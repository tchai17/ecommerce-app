package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.OrderService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * This class is the controller for the product management system. It handles
 * all product-related requests for the homepage like adding a new product, and
 * displaying all products.
 * 
 * @author - timothy.chai
 * 
 * @see Product
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	private Logger logger = LogManager.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	/**
	 * Handles requests for registration of a new product and directs the user to
	 * the product registration form
	 * 
	 * @return the name of the add product template
	 */
	@GetMapping("/add-product")
	public String addProduct() {
		logger.debug("User clicked on add-product button");
		return "addProduct";
	}

	/**
	 * Handles form inputs for new product registration and processes the
	 * registration of a new product.
	 * 
	 * @param request the HTTP request containing the product information
	 * @return the name of the completion template
	 */
	@PostMapping("/add-product")
	public String processRegistration(HttpServletRequest request) {
		logger.info("Request received to register new product");

		// Get parameters
		String productName = request.getParameter("productName");
		logger.debug("Product name input received: " + productName);
		String stockString = request.getParameter("stock");
		logger.debug("Stock input received: " + stockString);
		int stock = Integer.parseInt(stockString);
		String imgURL = request.getParameter("imgURL");
		logger.debug("Image URL input received: " + imgURL);
		String priceString = request.getParameter("price");
		logger.debug("Price input received: " + priceString);
		double price = Double.parseDouble(priceString);

		// Create new instance of product for persistence
		Product product = new Product(productName, stock, imgURL, price);
		logger.info("New product created: " + product);

		// Save onto database
		productService.saveProduct(product);
		logger.info("Product saved successfully onto database");

		logger.debug("Redirecting customer to registration-complete page");

		return "complete";
	}

	/**
	 * Handles requests after user login and redirection to the dashboard. Displays
	 * the products and categories on the dashboard.
	 * 
	 * @param selectedCategory the selected category, can be null
	 * @param model            the model object
	 * @return the name of the dashboard template
	 */
	@GetMapping("/dashboard")
	public String productDisplay(@RequestParam(required = false) String selectedCategory, Model model) {
		System.out.println("Customer is directed to dashboard");
		logger.debug("Customer is directed to dashboard");
		List<Product> products = new ArrayList<>();
		// Check if selectedCategory is null, or 'all', then display all products
		if (selectedCategory != null) {
			logger.info("Selected category: " + selectedCategory);

			if (selectedCategory.equalsIgnoreCase("all")) {
				logger.info("Selected category: " + selectedCategory + ", returning all products");
				products = productService.returnAllProducts();

			} else {
				// Use value of selectedCategory to filter products
				model.addAttribute("selectedCategory", selectedCategory);
				products = productService.findProductByCategory(selectedCategory);
			}

		} else {
			products = productService.returnAllProducts();
		}

		Map<Product, Integer> productMap = orderService.findPopularProducts();

		// if page is not loaded before, then set categories as an attribute to response
		Set<String> categories = productService.returnAllCategories();
		model.addAttribute("products", products);
		model.addAttribute("categories", categories);
		model.addAttribute("popularProducts", productMap);

		logger.debug("Products and categories added to response");
		return "dashboard";
	}

}