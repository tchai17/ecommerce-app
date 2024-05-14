package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomCustomerDetailsService implements UserDetailsService {

//    @Autowired
//    private CustomerRepository customerRepo;


    @Autowired
    private CustomerService customerService;

    /**
     * Loads the user's details from the database based on the username.
     *
     * @param username the username identifying the user to be loaded
     * @return a UserDetails object containing the user's data
     * @throws UsernameNotFoundException if no user could be found for the given username
     */


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       List<Customer> customers = customerService.findCustomerByUsername(username);
       System.out.println("CustomCustomerDetailsService called");
       Customer user = customers.get(0);

       return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")));



        // Fully qualify the Spring Security User class
        // Return a Spring Security UserDetails object that includes the user's username, password,
        // and granted authorities. Here we are assuming that every user has the authority 'USER'.


//        else {
//            List<SimpleGrantedAuthority> roles = new ArrayList<>();
//
//            roles.add(new SimpleGrantedAuthority("USER"));
//            roles.add(new SimpleGrantedAuthority("ADMIN"));
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    roles);
//        }



    }
}
