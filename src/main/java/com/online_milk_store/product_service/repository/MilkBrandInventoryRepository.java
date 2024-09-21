package com.online_milk_store.product_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.product_service.entity.MilkBrandInventoryEntity;

public interface MilkBrandInventoryRepository extends JpaRepository<MilkBrandInventoryEntity, Integer> {
	Optional<MilkBrandInventoryEntity> findByMilkBrandEntityMilkBrandId(Integer milkBrandId);
}
