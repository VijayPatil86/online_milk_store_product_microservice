package com.online_milk_store.product_service.entity;

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

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "DAIRY_PRODUCT")
public class DairyProductEntity {
	@Column(name = "DAIRY_PRODUCT_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dairyProductId;

	@Column(name = "DAIRY_PRODUCT_NAME")
	private String dairyProductName;

	@Column(name = "DAIRY_PRODUCT_AVAILABLE")
	private String dairyProductAvailable;

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID")
	private CategoryEntity categoryEntity;

	@Override
	public String toString() {
		return "DairyProductEntity [dairyProductId=" + dairyProductId + ", dairyProductName=" + dairyProductName
				+ ", dairyProductAvailable=" + dairyProductAvailable + ", categoryEntity=" + categoryEntity + "]";
	}
}
