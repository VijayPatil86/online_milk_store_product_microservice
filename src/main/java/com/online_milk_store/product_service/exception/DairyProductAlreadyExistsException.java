package com.online_milk_store.product_service.exception;

public class DairyProductAlreadyExistsException extends RuntimeException {
	public DairyProductAlreadyExistsException(String message) {
		super(message);
	}
}
