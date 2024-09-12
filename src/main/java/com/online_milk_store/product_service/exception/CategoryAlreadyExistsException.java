package com.online_milk_store.product_service.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
	public CategoryAlreadyExistsException(String message) {
		super(message);
	}
}
