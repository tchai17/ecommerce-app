package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	private CustomerService customerService;

	@Mock
	private CustomerRepository mockCustomerRepo;

	private Customer customer1;

	@BeforeEach
	void setUp() {
		customerService = new CustomerService(mockCustomerRepo);
		customer1 = new Customer("Tim1", "pw1", "tim@example.com", "Singapore", "Timothy", "1234123412341234");

	}

	@Test
	@DisplayName("1. Test that CustomerService.save calls CustomerRepository.save")
	void testSaveNewCustomer() {
		// Arrange
		Mockito.when(mockCustomerRepo.save(customer1)).thenReturn(customer1);

		// Act
		customerService.saveNewCustomer(customer1);
		// Assert
		Mockito.verify(mockCustomerRepo).save(customer1);
	}

	@Test
	@DisplayName("2. Test that CustomerService.update calls CustomerRepository.save")
	void testUpdateCustomer() {

		// Arrange
		Mockito.when(mockCustomerRepo.save(customer1)).thenReturn(customer1);
		// Act
		customerService.updateCustomer(customer1);
		// Assert
		Mockito.verify(mockCustomerRepo).save(customer1);
	}

}
