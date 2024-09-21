package com.online_milk_store.product_service.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class MilkBrandPurchaseBean {
	private int detailId;
	private float purchasePrice;
	private int purchaseQuantity;
	private float totalPurchasePrice;
	private String purchaseDateTime;
	private MilkBrandInventoryBean milkBrandInventoryBean;
}
