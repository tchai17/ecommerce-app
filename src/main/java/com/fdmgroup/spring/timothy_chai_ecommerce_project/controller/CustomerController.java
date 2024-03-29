package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Cart;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
@Transactional(readOnly = true)
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
    private HttpSession httpSession; 
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/register-customer") // localhost:8080/customer/register-customer
    public String customers() {
        return "registerCustomer";
    }
    
    @GetMapping("/login") // localhost:8080/customer/login
    public String customerLogin() {
        return "customerLogin";
    }
    
    @PostMapping("/registration") // localhost:8080/customer/registration
    public String processRegistration(HttpServletRequest request) {
    	System.out.println("Initiate registration for new customer");
    	
    	// Get parameters
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String email = request.getParameter("email");
    	String address = request.getParameter("address");
    	String fullName = request.getParameter("fullName");
    	String cardNumber = request.getParameter("cardNumber");
    	
    	// Create new Customer instance
    	Customer newCustomer = new Customer(username, password, email, address, fullName, cardNumber);
    	
    	// Save to DB
    	customerService.saveNewCustomer(newCustomer);
    	System.out.println(newCustomer);
    	System.out.println("Registration Complete");
    	
    	return "complete";
    }
    
    
    @PostMapping("/login-customer") // localhost:8080/customer/login-customer
    public String processLogin(HttpServletRequest request) {
    	System.out.println("Initiate login for existing customer");
        
        // Get parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Check if username already exists in database
        List<Customer> existingCustomers = customerService.findCustomerByUsername(username);
        if ( existingCustomers.isEmpty() ) {
            System.out.println("Username does not exist in database");
            return "customerLogin";
        }
        
        // Check if password is correct
        if ( existingCustomers.get(0).getPassword().equals(password) ) {
            System.out.println("Password is correct");
            Customer currentCustomer = existingCustomers.get(0);
            Cart cart = currentCustomer.getCart(); // Retrieve current cart
            
            System.out.println(cart);
            
            httpSession.setAttribute("customer", currentCustomer);
            httpSession.setAttribute("cart", cart);           
            httpSession.setAttribute("isLoggedIn", true);
            return "redirect:/product/dashboard";
        }
        else {
            System.out.println("Password is incorrect");
            return "customerLogin";
        }
    }

    @Transactional(readOnly = false)
    @PostMapping("/addToCart")
    public String addToCart(@SessionAttribute Customer customer, int productId, @RequestParam int quantity) {
    	// Get relevant product and customer that is logged in
    	Optional<Product> product = productService.findProductById(productId);
    	Customer target = customerService.findCustomerByID(customer.getCustomerID()).get();
    	
    	// if product is found
    	if ( product.isPresent() ) {
    		// Get customer's cart and set as session attribute
    		Cart cart = target.getCart();
    		httpSession.setAttribute("cart", cart);
    		
    		// Add to cart and update
    		cart.addToCart( new CartItem(product.get(),quantity) );
    		cart.updateTotalPrice();
    		
    		// Save as customer
    		customer.setCart(cart);
    		customerService.updateCustomer(customer);
    		System.out.println("Item added");
        }
    	return "redirect:/product/dashboard";
    }
    
}
