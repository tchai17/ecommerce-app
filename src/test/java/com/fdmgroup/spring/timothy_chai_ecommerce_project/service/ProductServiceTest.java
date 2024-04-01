package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.Product;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.ProductRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	public ProductService productService;
	private Product product1;
	private Product product2;

	@Mock
	private ProductRepository mockProductRepo;

	@BeforeEach
	public void setup() {
		product1 = new Product("Product 1", 99, "http://example.com/image1.jpg", 10.00);
		product2 = new Product("Product 2", 99, "http://example.com/image2.jpg", 5.00);
		productService = new ProductService(mockProductRepo);

	}

	@Test
	@DisplayName("1. Test that ProductService.saveProduct() calls ProductRepository.save()")
	public void test_saveProduct_calls_ProductRepoSave() {

		// Arrange
		Mockito.when(mockProductRepo.save(product1)).thenReturn(product1);
		// Act
		productService.saveProduct(product1);
		// Assert
		Mockito.verify(mockProductRepo).save(product1);

	}

	@Test
	@DisplayName("2. Test when saveProduct() is called on an existing product, it does not call ProductRepository.save()")
	public void test_saveProduct_does_not_call_ProductRepoSave_onRepeat() {

		// Arrange
		List<Product> products = new ArrayList<>();
		products.add(product1);
		Mockito.when(mockProductRepo.findByProductName("Product 1")).thenReturn(products);

		// Act
		productService.saveProduct(product1);
		// Assert
		Mockito.verify(mockProductRepo, Mockito.never()).save(product1);

	}

	@Test
	@DisplayName("3. Test when updateProduct() is called on an existing product, it calls ProductRepository.save")
	public void test_updateProduct_calls_ProductRepoSave() {

		// Arrange
		Product updatedProduct = new Product("Updated Product", 99, "http://example.com/updated-image.jpg", 15.00);
		updatedProduct.setProductID(product1.getProductID());

		List<Product> expected = new ArrayList<>();
		expected.add(updatedProduct);
		Mockito.when(mockProductRepo.findById(product1.getProductID())).thenReturn(Optional.ofNullable(product1));

		// Act
		productService.updateProduct(updatedProduct);

		// Assert
		Mockito.verify(mockProductRepo).save(updatedProduct);

	}

	@Test
	@DisplayName("4. Test when findProductById is called, it calls ProductRepository.findById")
	public void test_findProductById_calls_ProductRepo_findById() {
		// Arrange
		Mockito.when(mockProductRepo.findById(product1.getProductID())).thenReturn(Optional.ofNullable(product1));
		Optional<Product> expected = Optional.ofNullable(product1);
		// Act
		Optional<Product> actual = productService.findProductById(product1.getProductID());
		// Assert
		assertEquals(expected, actual);

	}

	@Test
	@DisplayName("5. Test when findProductByProductName is called, it calls ProductRepository.findByProductName")
	public void test_findProductByProductName_calls_ProductRepo_findByProductName() {
		// Arrange
		List<Product> products = new ArrayList<>();
		products.add(product1);
		Mockito.when(mockProductRepo.findByProductName("Product 1")).thenReturn(products);
		// Act
		List<Product> actual = productService.findProductByProductName(product1.getProductName());
		// Assert
		assertEquals(products, actual);

	}

	@Test
	@DisplayName("6. Test when findProductById is called, it calls ProductRepository.findAll")
	public void test_returnAllProducts_calls_ProductRepo_findAll() {
		// Arrange
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(product1);
		allProducts.add(product2);
		Mockito.when(mockProductRepo.findAll()).thenReturn(allProducts);
		// Act
		List<Product> actual = productService.returnAllProducts();
		// Assert

		assertEquals(allProducts, actual);
		assertTrue(actual.contains(product1));
		assertTrue(actual.contains(product2));
	}

}
