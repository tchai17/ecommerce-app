package com.fdmgroup.spring.timothy_chai_ecommerce_project.configuration;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CustomAuthenticationSuccessHandler;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomCustomerDetailsService;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomCustomerDetailsService customCustomerDetailsService;

    @Autowired
    private CustomerService customerService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/customer/register-customer", "/styles.css")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/customer/login")
                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/product/dashboard", true)
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/"));
        return http.build();
    }



    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<Customer> customers = customerService.findCustomerByUsername(username);
        System.out.println("CustomCustomerDetailsService called");
        Customer user = customers.get(0);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(WebSecurity webSecurity) throws Exception{
        webSecurity.ignoring()
                .requestMatchers("/static/**");
    }


}
