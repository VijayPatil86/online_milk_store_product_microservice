package com.online_milk_store.product_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.product_service.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
	List<CategoryEntity> findByCategoryAvailableIs(String categoryAvailableStatus);
	Optional<CategoryEntity> findByCategoryNameIs(String categoryName);
}
