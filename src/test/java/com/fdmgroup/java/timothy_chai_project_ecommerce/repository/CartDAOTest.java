package com.fdmgroup.java.timothy_chai_project_ecommerce.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Cart;
import com.fdmgroup.java.timothy_chai_project_ecommerce.app.CartItem;
import com.fdmgroup.java.timothy_chai_project_ecommerce.app.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;


/** Responsible for testing CartDAO methods
 * 
 * @author - timothy.chai
 * 
 * @see CartDAO
 */
@ExtendWith(MockitoExtension.class)
public class CartDAOTest {

	Cart cart;
	CartDAO cartDAO;
	@Mock EntityManagerFactory mockEMF;
	@Mock EntityManager mockEM;
	@Mock EntityTransaction mockTransaction;
	
	@BeforeEach
	void setUp() {
		cartDAO = new CartDAO(mockEMF);
		when(mockEMF.createEntityManager()).thenReturn(mockEM);
		cart = new Cart();
		cart.setCartID(1);
    }
	
	@Test
	@DisplayName("1. Test that cartDAO calls entityManager.persist()")
	void testCartDAOCalls_entityManagerPersist() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        
        // act
        cartDAO.persist(cart);
        
        // assert
        verify(mockEM).persist(cart);	
		
	}
	
	@Test
	@DisplayName("2. Test that CartDAO addProductToCart adds correct item to cart")
	void testAddProductToCart_adds_correct_item_to_cart() {
		// arrange
        when(mockEM.getTransaction()).thenReturn(mockTransaction);
        CartItem cartItem1 = new CartItem( new Product(), 1 );
        Set<CartItem> expected = new HashSet<>();
        expected.add(cartItem1);

        Set<CartItem> actual;
        
        // act
        cartDAO.addProductToCart(cart, cartItem1);
        actual = cart.getItems();
        
        // assert
        assertEquals(expected, actual);;
	}
	
	@Test
	@DisplayName("3. Test that CartDAO removeProductFromCart removes correct item from cart")
    void testRemoveProductFromCart_removes_correct_item_from_cart() {
		// arrange
		when(mockEM.getTransaction()).thenReturn(mockTransaction);
        Product product1 = new Product("product1", 5, "product1.html", 5.00);
        Product product2 = new Product("product2", 10, "product2.html", 4.00);
		CartItem cartItem1 = new CartItem( product1, 3 );
        CartItem cartItem2 = new CartItem( product2, 4 );
        Set<CartItem> expected = new HashSet<>();
        expected.add(cartItem1);
        
        Set<CartItem> setup = new HashSet<>();
        setup.add(cartItem1);
        setup.add(cartItem2);
        cart.setItems(setup);
        
        Set<CartItem> actual;
        
        // act
        cartDAO.removeProductFromCart(cart, cartItem2);
        actual = cart.getItems();
        
        // assert
        assertEquals(expected, actual);
	}
	
	
}
