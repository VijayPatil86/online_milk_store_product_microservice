package com.online_milk_store.product_service.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class DairyProductBean {
	private int dairyProductId;
	private String dairyProductName;
	private String dairyProductAvailable;
}
