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

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;


/** Responsible for testing ProductDAO methods
 * 
 * @author - timothy.chai
 * 
 * @see ProductDAO
 */
@ExtendWith(MockitoExtension.class)
public class ProductDAOTest {

	Product product1;
	ProductDAO productDAO;
	@Mock EntityManagerFactory mockEMF;
	@Mock EntityManager mockEM;
	@Mock EntityTransaction mockTransaction;
	@Mock Query mockQuery;
	
	@BeforeEach
	void setUp() {
		productDAO = new ProductDAO(mockEMF);
		when(mockEMF.createEntityManager()).thenReturn(mockEM);
		product1 = new Product("product1", 5, "link.html", 5.00);
		product1.setProductID(1);
    }
	
	@Test
	@DisplayName("1. Check that ProductDAO persist calls EntityManager.persist()")
	void testProductDAOcalls_entityManagerPersist() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        
        // act
        productDAO.persist(product1);
        
        // assert
        verify(mockEM).persist(product1);;
	}
	
	@Test
	@DisplayName("2. Check that ProductDAO remove removes correct product")
	void testProductDAOremoveRemovesCorrectProduct() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        
        // act
        productDAO.remove(product1);
        
        // assert
        verify(mockEM).remove(product1);;
	}
	
	@Test
	@DisplayName("3. Check that ProductDAO findById() returns correct product")
	void testProductDAOfindByIdReturnsCorrectProduct() {
			// arrange
        when(mockEM.find(Product.class, 1)).thenReturn(product1);
       
        
        // act
        Product actual = productDAO.findProductById(1);
        
        // assert
        assertEquals(product1, actual);
	}
	
	
	
}
