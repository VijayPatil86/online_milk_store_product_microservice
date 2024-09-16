package com.online_milk_store.product_service.bean;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class DairyProductBean extends RepresentationModel<DairyProductBean>{
	private int dairyProductId;
	private String dairyProductName;
	private String dairyProductAvailable;
}
