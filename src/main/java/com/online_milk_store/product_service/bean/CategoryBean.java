package com.online_milk_store.product_service.bean;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class CategoryBean extends RepresentationModel<CategoryBean>{
	private int categoryId;
	private String categoryName;
	private String categoryAvailable;
}
