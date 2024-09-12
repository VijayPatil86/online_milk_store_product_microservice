package com.online_milk_store.product_service.bean;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class CategoryContainer extends RepresentationModel<CategoryContainer>{
	private List<CategoryBean> categoryBeans;
}
