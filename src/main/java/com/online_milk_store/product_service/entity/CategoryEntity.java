package com.online_milk_store.product_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity {
	@Column(name = "CATEGORY_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int categoryId;
	
	@Column(name = "CATEGORY_NAME")
	private String categoryName;
	
	@Column(name = "CATEGORY_AVAILABLE")
	private String categoryAvailable;
}
