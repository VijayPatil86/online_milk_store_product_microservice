package com.online_milk_store.product_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.product_service.entity.MilkBrandEntity;

public interface MilkBrandRepository extends JpaRepository<MilkBrandEntity, Integer> {
	List<MilkBrandEntity> findByMilkBrandAvailableIs(String milkBrandAvailableStatus);
}
