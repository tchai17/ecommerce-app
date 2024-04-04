package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartItemRepository;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

	@Mock
	private CartItemRepository cartItemRepo;

	@InjectMocks
	private CartItemService cartItemService;

	@Test
	@DisplayName("1. Test that deleteCartItemFromDatabase calls CartItemRepo.delete")
	public void testDeleteCartItemFromDatabase() {
		// Prepare dummy data
		CartItem cartItem = new CartItem();
		cartItem.setCartItemId(1);

		when(cartItemRepo.findById(any())).thenReturn(Optional.of(cartItem));

		// Test
		cartItemService.deleteCartItemFromDatabase(cartItem);

		// Verify that the delete method is called with the correct parameter
		verify(cartItemRepo).delete(cartItem);
	}

	@Test
	@DisplayName("2. Test that deleteCartItemFromDatabase does not call CartItemRepo.delete when CartItem is not found")
	public void testDeleteCartItemFromDatabaseNotFound() {
		// Prepare dummy data
		CartItem cartItem = new CartItem();
		cartItem.setCartItemId(1);

		when(cartItemRepo.findById(any())).thenReturn(Optional.empty());

		// Test
		cartItemService.deleteCartItemFromDatabase(cartItem);

		// Verify that the delete method is not called when item is not found
		verify(cartItemRepo).findById(cartItem.getCartItemId());
		verify(cartItemRepo, never()).delete(cartItem);
	}
}
