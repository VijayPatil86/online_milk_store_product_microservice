package com.online_milk_store.product_service.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class CategoryBean {
	private int categoryId;
	private String categoryName;
	private String categoryAvailable;
}
