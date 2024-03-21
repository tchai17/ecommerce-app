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

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;
import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@ExtendWith(MockitoExtension.class)
public class CartItemDAOTest {

	CartItem cartItem1;
	CartItemDAO cartItemDAO;
	@Mock EntityManagerFactory mockEMF;
	@Mock EntityManager mockEM;
	@Mock EntityTransaction mockTransaction;
	@Mock Query mockQuery;
	
	@BeforeEach
	void setUp()  {
		cartItemDAO = new CartItemDAO(mockEMF);
		when(mockEMF.createEntityManager()).thenReturn(mockEM);
		cartItem1 = new CartItem(new Product(), 1);
    }
	
	@Test
    @DisplayName("1. Test that cartItemDAO calls entityManager.persist()")
    void testCartItemDAOCalls_entityManagerPersist() {
		// arrange
		when(mockEM.getTransaction()).thenReturn(mockTransaction);
        
        // act
        cartItemDAO.persist(cartItem1);
        
        // assert
        verify(mockEM).persist(cartItem1);
    
	}
	
	@Test
	@DisplayName("2. Test that cartItemDAO remove removes the correct item")
	void testCartItemDAOremoveRemoves_correct_item() {
		// arrange
		when(mockEM.getTransaction()).thenReturn(mockTransaction);
		cartItemDAO.persist(cartItem1);
        
        // act
        cartItemDAO.remove(cartItem1);
        
        // assert
        verify(mockEM).remove(cartItem1);
	}
	
	@Test
	@DisplayName("3. Test that cartItemDAO findById returns correct item")
	void testCartItemDAOfindByIdReturnsCorrectItem() {
		// arrange
        when(mockEM.find(CartItem.class, 1)).thenReturn(cartItem1);
        
        // act
        CartItem actual = cartItemDAO.findById(1);
        
        // assert
        assertEquals(cartItem1, actual);
	}

	
	
}
