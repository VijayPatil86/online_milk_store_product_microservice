package com.online_milk_store.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_milk_store.product_service.entity.MilkBrandPurchaseEntity;

public interface MilkBrandPurchaseRepository extends JpaRepository<MilkBrandPurchaseEntity, Integer> {

}
