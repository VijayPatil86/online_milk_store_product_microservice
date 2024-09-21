package com.online_milk_store.product_service.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class MilkBrandInventoryBean {
	private int inventotyId;
	private int currentQuantity;
	private int minimumQuantity;
	private float currentPurchasePrice;
	private float currentSellPrice;
	private String outOfStock;
}
