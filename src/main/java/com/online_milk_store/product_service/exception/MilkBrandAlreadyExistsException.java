package com.online_milk_store.product_service.exception;

public class MilkBrandAlreadyExistsException extends RuntimeException {
	public MilkBrandAlreadyExistsException(String message) {
		super(message);
	}
}
