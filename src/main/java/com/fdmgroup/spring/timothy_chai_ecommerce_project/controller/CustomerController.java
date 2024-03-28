package com.fdmgroup.spring.timothy_chai_ecommerce_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
    @Autowired
    private ProductService productService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/register-customer")
    public String customers() {
        return "registerCustomer";
    }
    
    @GetMapping("/login")
    public String customerLogin() {
        return "customerLogin";
    }
    
    @PostMapping("/registration")
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
    	
    	// Check if username already exists in database
    	List<Customer> existingCustomers = customerService.findCustomerByUsername(username);
    	if ( !existingCustomers.isEmpty() ) {
    		System.out.println("Username already exists in database");
    		return "registerCustomer";
    	}
    	
    	// Save to DB
    	customerService.saveCustomer(newCustomer);
    	System.out.println(newCustomer);
    	System.out.println("Registration Complete");
    	
    	return "complete";
    }
    
    @PostMapping("/login-customer")
    public String processLogin(HttpServletRequest request, HttpSession session) {
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
            session.setAttribute("username", existingCustomers.get(0).getUsername() );
            return "customerHome";
        }
        else {
            System.out.println("Password is incorrect");
            return "customerLogin";
        }
    }
	
}
