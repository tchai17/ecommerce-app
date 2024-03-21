package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@ExtendWith(MockitoExtension.class)
public class CustomerDAOTest {
	
	
	Customer customer1;
	CustomerDAO customerDAO;
	@Mock EntityManagerFactory mockEMF;
	@Mock EntityManager mockEM;
	@Mock EntityTransaction mockTransaction;
	@Mock Query mockQuery;
	
	@BeforeEach
	void setUp() {
		customerDAO = new CustomerDAO(mockEMF);
		when(mockEMF.createEntityManager()).thenReturn(mockEM);
		customer1 = new Customer("customer1", "password", "email", "address", "fullName", "cardNumber");
		customer1.setUserID(1);
		
    }
	
	@Test
	@DisplayName("1. Test if CustomerDAO calls EntityManager.persist()")
	void testCustomerDAOcalls_EMPersist() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        
        // act
        customerDAO.persist(customer1);
        
        // assert
        verify(mockEM).persist(customer1);
	}
	
	@Test
	@DisplayName("2. Test if CustomerDAO update sends correct details")
	void testCustomerDAOupdateSendsCorrectDetails() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        when(mockEM.find(Customer.class, 1)).thenReturn(customer1);
        Customer newCustomer1 = new Customer("customer1-new", "password123", "email", "address", "fullName123", "cardNumber");
        newCustomer1.setUserID(1);
        
        // act
        customerDAO.update(newCustomer1);
        
        // assert
        assertEquals(newCustomer1, customer1);
	}
	
	@Test
	@DisplayName("3. Test if CustomerDAO findById returns correct customer")
	void testCustomerDAO_findById_ReturnsCorrectCustomer() {
		// arrange
		when(mockEM.find(Customer.class, 1)).thenReturn(customer1);
		when(mockEM.getTransaction()).thenReturn(mockTransaction);
		
		// act
		Customer actual = customerDAO.findCustomerById(1);
		
		// assert
		assertEquals(customer1, actual);
		
		
	}
	
	@Test
	@DisplayName("4. Test if CustomerDAO findByUsername returns correct result")
	void testCustomerDAO_findByUsername_ReturnsCorrectResult() {
		// arrange
        when(mockEM.createQuery("SELECT c FROM Customer c WHERE username LIKE :username")).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(customer1);
        
        
        // act
        Customer actual = customerDAO.findCustomerByUsername("customer1");
        
        // assert
        assertEquals(customer1, actual);
	}

}
