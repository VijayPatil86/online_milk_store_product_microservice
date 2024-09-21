package com.online_milk_store.product_service.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@PropertySource("classpath:business-props.properties")
public class BusinessProperties {
	@Value("${quantity.minimum}")
	private int quantityMinimum;
}
