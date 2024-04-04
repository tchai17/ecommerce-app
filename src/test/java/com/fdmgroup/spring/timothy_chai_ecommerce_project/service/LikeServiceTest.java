package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Customer;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CustomerRepository;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

	public LikeService likeService;

	@Mock
	CustomerRepository mockCustomerRepo;

	@Mock
	ProductRepository mockProductRepo;

	private Customer customer;
	private Product product;

	@BeforeEach
	public void init() {
		likeService = new LikeService(mockCustomerRepo, mockProductRepo);
		customer = new Customer("customer1", "password", "customer@email.com", "address1", "Customer", "1234");
		product = new Product("product1", 50, "img.url", 5.00);

		when(mockCustomerRepo.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
		when(mockProductRepo.findById(product.getProductID())).thenReturn(Optional.of(product));
	}

	@Test
	@DisplayName("1. Test if both customer and product exists, addToLikes calls customerRepo.save")
	public void addToLikes_whenCustomerAndProductExists_shouldAddProductToCustomerLikes() {
		// arrange

		// act
		likeService.addToLikes(customer, product);

		// assert
		verify(mockCustomerRepo).save(customer);
		assertTrue(customer.getLikes().contains(product));
	}

	@Test
	@DisplayName("2. Test that if customer does not exist in database, product is not added to likes")
	public void addToLikes_whenCustomerDoesNotExist_shouldNotAddProductToCustomerLikes() {
		// arrange
		Mockito.when(mockCustomerRepo.findById(customer.getCustomerID())).thenReturn(Optional.empty());

		// act
		likeService.addToLikes(customer, product);

		// assert
		assertFalse(customer.getLikes().contains(product));
	}

	@Test
	@DisplayName("3. Test that if product does not exist in database, product is not added to likes")
	public void addToLikes_whenProductDoesNotExist_shouldNotAddProductToCustomerLikes() {
		// arrange
		when(mockProductRepo.findById(product.getProductID())).thenReturn(Optional.empty());

		// act
		likeService.addToLikes(customer, product);

		// assert
		assertFalse(customer.getLikes().contains(product));
	}

	@Test
	@DisplayName("4. Test if both customer and product exists, removeFromLikes calls customerRepo.save")
	public void removeFromLikes_whenCustomerAndProductExists_shouldRemoveProductFromCustomerLikes() {
		// arrange

		// act
		likeService.removeFromLikes(customer, product);

		// assert
		verify(mockCustomerRepo).save(customer);
		assertFalse(customer.getLikes().contains(product));
	}

	@Test
	@DisplayName("5. Test that if customer does not exist in database, product is not removed from likes")
	public void removeFromLikes_whenCustomerDoesNotExist_shouldNotRemoveProductFromCustomerLikes() {
		// arrange
		Mockito.when(mockCustomerRepo.findById(customer.getCustomerID())).thenReturn(Optional.empty());
		Set<Product> likes = new HashSet<Product>();
		likes.add(product);
		customer.setLikes(likes);

		// act
		likeService.removeFromLikes(customer, product);

		// assert
		assertTrue(customer.getLikes().contains(product));
	}

	@Test
	@DisplayName("6. Test that if product does not exist in database, product is not removed from likes")
	public void removeFromLikes_whenProductDoesNotExist_shouldNotRemoveProductFromCustomerLikes() {
		// arrange
		when(mockProductRepo.findById(product.getProductID())).thenReturn(Optional.empty());
		Set<Product> likes = new HashSet<Product>();
		likes.add(product);
		customer.setLikes(likes);

		// act
		likeService.removeFromLikes(customer, product);

		// assert
		assertTrue(customer.getLikes().contains(product));
	}
}
