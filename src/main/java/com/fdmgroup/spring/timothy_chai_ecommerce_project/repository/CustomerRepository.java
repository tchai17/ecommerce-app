package com.fdmgroup.spring.timothy_chai_ecommerce_project.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;

@Repository
public interface CustomerRepository extends InterfaceRepository<Customer, Integer>{

	public List<Customer> findByUsername(String username);
	public List<Customer> findByEmail(String email);
	public List<Customer> findByAddress(String address);
	public List<Customer> findByFullName(String fullName);
	
}
