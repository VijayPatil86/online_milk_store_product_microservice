package com.online_milk_store.product_service.exception;

public class CategoryNotAvailableException extends RuntimeException {
	public CategoryNotAvailableException(String message) {
		super(message);
	}
}
