package com.fdmgroup.spring.timothy_chai_ecommerce_project.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring.timothy_chai_ecommerce_project.model.CartItem;
import com.fdmgroup.spring.timothy_chai_ecommerce_project.repository.CartItemRepository;

/**
 * This class provides business logic for CartItem entities.
 */
@Service
public class CartItemService {

	private Logger logger = LogManager.getLogger(CartItemService.class);

	private CartItemRepository cartItemRepo;

	/**
	 * Constructs a new CartItemService with the given CartItemRepository.
	 *
	 * @param cartItemRepo the CartItemRepository to use for data access
	 */
	public CartItemService(CartItemRepository cartItemRepo) {
		this.cartItemRepo = cartItemRepo;
	}

	/**
	 * Deletes the given CartItem from the database.
	 *
	 * @param itemToDelete the CartItem to delete
	 */
	public void deleteCartItemFromDatabase(CartItem itemToDelete) {
		logger.debug("deleteCartItemFromDatabase is called for item: " + itemToDelete);
		Optional<CartItem> target = cartItemRepo.findById(itemToDelete.getCartItemId());
		if (target.isPresent()) {
			logger.debug("Matching item is deleted: " + target.get());
			cartItemRepo.delete(target.get());
		}

	}

}