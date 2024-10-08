package com.online_milk_store.product_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.product_service.entity.DairyProductEntity;

public interface DairyProductRepository extends JpaRepository<DairyProductEntity, Integer> {
	List<DairyProductEntity> findByDairyProductAvailableIs(String dairyProductAvailableStatus);
	Optional<DairyProductEntity> findByDairyProductNameIs(String dairyProductName);
}
