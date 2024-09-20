package com.online_milk_store.product_service.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "MILK_BRAND_BUY_DETAILS")
public class MilkBrandPurchaseEntity {
	@Column(name = "DETAIL_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int detailId;

	@Column(name = "MILK_BRAND_BUY_PRICE")
	private float purchasePrice;

	@Column(name = "MILK_BRAND_BUY_QUANTITY")
	private int purchaseQuantity;

	@Column(name = "MILK_BRAND_TOTAL_BUY_PRICE")
	private float totalPurchasePrice;

	@Column(name = "BUY_TIMESTAMP")
	private Timestamp purchaseDateTime;

	@ManyToOne
	@JoinColumn(name = "MILK_BRAND_ID")
	private MilkBrandEntity milkBrandEntity;
}
