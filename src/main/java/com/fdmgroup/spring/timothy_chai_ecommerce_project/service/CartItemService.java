package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartItemRepository;

@Service
public class CartItemService {

	private CartItemRepository cartItemRepo;
	
	public CartItemService(CartItemRepository cartItemRepo) {
		this.cartItemRepo = cartItemRepo;
	}
	
	public void deleteCartItemFromDatabase(CartItem itemToDelete) {
		cartItemRepo.delete(itemToDelete);
	}
	
}
