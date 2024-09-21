package com.online_milk_store.product_service.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.BusinessProperties;
import com.online_milk_store.product_service.bean.MilkBrandPurchaseBean;
import com.online_milk_store.product_service.entity.MilkBrandEntity;
import com.online_milk_store.product_service.entity.MilkBrandInventoryEntity;
import com.online_milk_store.product_service.entity.MilkBrandPurchaseEntity;
import com.online_milk_store.product_service.exception.MilkBrandNotAvailableException;
import com.online_milk_store.product_service.repository.MilkBrandInventoryRepository;
import com.online_milk_store.product_service.repository.MilkBrandPurchaseRepository;
import com.online_milk_store.product_service.repository.MilkBrandRepository;

@Service
@Transactional
public class MilkBrandPurchaseService {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandPurchaseService.class);

	@Autowired
	private BusinessProperties businessProperties;

	@Autowired
	private MilkBrandRepository milkBrandRepository;

	@Autowired
	private MilkBrandPurchaseRepository milkBrandPurchaseRepository;

	@Autowired
	private MilkBrandInventoryRepository milkBrandInventoryRepository;

	public void addPurchaseRecord(int milkBrandId, MilkBrandPurchaseBean milkBrandPurchaseBean) {
		LOGGER.debug("MilkBrandPurchaseService.addPurchaseRecord() --- START");
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- milkBrandId: " + milkBrandId);
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- milkBrandPurchaseBean: " + milkBrandPurchaseBean);
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- validating Milk Brand Id: " + milkBrandId);
		MilkBrandEntity milkBrandEntity = milkBrandRepository.findById(milkBrandId)
				.orElseThrow(() -> new MilkBrandNotAvailableException("Milk Brand with id " + milkBrandId + " not found"));
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- Milk Brand entity with id: " + milkBrandId +
				" found, Milk Brand entity is: " + milkBrandEntity);
		MilkBrandPurchaseEntity milkBrandPurchaseEntity = MilkBrandPurchaseEntity.builder()
				.purchasePrice(milkBrandPurchaseBean.getPurchasePrice())
				.purchaseQuantity(milkBrandPurchaseBean.getPurchaseQuantity())
				.totalPurchasePrice(milkBrandPurchaseBean.getPurchasePrice() * milkBrandPurchaseBean.getPurchaseQuantity())
				.purchaseDateTime(new Timestamp(System.currentTimeMillis()))
				.build();
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- milkBrandPurchaseEntity: " + milkBrandPurchaseEntity);
		milkBrandPurchaseEntity.setMilkBrandEntity(milkBrandEntity);
		MilkBrandPurchaseEntity savedMilkBrandPurchaseEntity = milkBrandPurchaseRepository.save(milkBrandPurchaseEntity);
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- savedMilkBrandPurchaseEntity: " + savedMilkBrandPurchaseEntity);
		LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- finding inventory record for milk brand id: " + milkBrandId);
		Optional<MilkBrandInventoryEntity> optionalMilkBrandInventoryEntity = milkBrandInventoryRepository.findByMilkBrandEntityMilkBrandId(milkBrandId);
		if(optionalMilkBrandInventoryEntity.isEmpty()) {
			LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- inventory record for milk brand id: " +
					milkBrandId + " not exists, adding the record");
			MilkBrandInventoryEntity milkBrandInventoryEntity = MilkBrandInventoryEntity.builder()
					.currentQuantity(milkBrandPurchaseBean.getPurchaseQuantity())
					.minimumQuantity(businessProperties.getQuantityMinimum())
					.currentPurchasePrice(milkBrandPurchaseBean.getPurchasePrice())
					.currentSellPrice(milkBrandPurchaseBean.getMilkBrandInventoryBean().getCurrentSellPrice())
					.outOfStock("N")
					.build();
			LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- milkBrandInventoryEntity: " + milkBrandInventoryEntity);
			milkBrandInventoryEntity.setMilkBrandEntity(milkBrandEntity);
			MilkBrandInventoryEntity createdMilkBrandInventoryEntity = milkBrandInventoryRepository.save(milkBrandInventoryEntity);
			LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- createdMilkBrandInventoryEntity: " + createdMilkBrandInventoryEntity);
		} else {
			MilkBrandInventoryEntity existingMilkBrandInventoryEntity = optionalMilkBrandInventoryEntity.get();
			LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- existingMilkBrandInventoryEntity: " + existingMilkBrandInventoryEntity);
			existingMilkBrandInventoryEntity.setCurrentPurchasePrice(milkBrandPurchaseBean.getPurchasePrice());
			existingMilkBrandInventoryEntity.setOutOfStock("N");
			existingMilkBrandInventoryEntity.setMinimumQuantity(businessProperties.getQuantityMinimum());
			existingMilkBrandInventoryEntity.setCurrentQuantity(existingMilkBrandInventoryEntity.getCurrentQuantity() +
					milkBrandPurchaseBean.getPurchaseQuantity());
			existingMilkBrandInventoryEntity.setCurrentSellPrice(milkBrandPurchaseBean.getMilkBrandInventoryBean().getCurrentSellPrice());
			existingMilkBrandInventoryEntity.setMilkBrandEntity(milkBrandEntity);
			MilkBrandInventoryEntity updatedMilkBrandInventoryEntity = milkBrandInventoryRepository.save(existingMilkBrandInventoryEntity);
			LOGGER.info("MilkBrandPurchaseService.addPurchaseRecord() --- updatedMilkBrandInventoryEntity: " + updatedMilkBrandInventoryEntity);
		}
		LOGGER.debug("MilkBrandPurchaseService.addPurchaseRecord() --- END");
	}
}
