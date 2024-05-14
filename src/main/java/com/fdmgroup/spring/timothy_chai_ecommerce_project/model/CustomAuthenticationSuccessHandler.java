package com.fdmgroup.spring.timothy_chai_ecommerce_project.model;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("authentication success");
        String username = authentication.getName();
        Customer customer = customerService.findCustomerByUsername(username).get(0); // Assuming unique username

        HttpSession session = request.getSession();
        session.setAttribute("customer", customer);
        session.setAttribute("cart", customer.getCart());
        session.setAttribute("likes", customer.getLikes());
        session.setAttribute("orders", customer.getOrders());
        session.setAttribute("isLoggedIn", true);

        response.sendRedirect("/product/dashboard");
    }
}
