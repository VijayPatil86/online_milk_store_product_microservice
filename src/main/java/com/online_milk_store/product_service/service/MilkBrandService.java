package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.MilkBrandBean;
import com.online_milk_store.product_service.entity.DairyProductEntity;
import com.online_milk_store.product_service.entity.MilkBrandEntity;
import com.online_milk_store.product_service.exception.DairyProductNotAvailableException;
import com.online_milk_store.product_service.exception.MilkBrandAlreadyExistsException;
import com.online_milk_store.product_service.exception.MilkBrandNotAvailableException;
import com.online_milk_store.product_service.exception.MilkBrandsNotAvailableException;
import com.online_milk_store.product_service.repository.DairyProductRepository;
import com.online_milk_store.product_service.repository.MilkBrandRepository;

@Service
@Transactional
public class MilkBrandService {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandService.class);

	@Autowired
	private MilkBrandRepository milkBrandRepository;

	@Autowired
	private DairyProductRepository dairyProductRepository;

	@Transactional(readOnly = true)
	public List<MilkBrandBean> getAllAvailableMilkBrands() {
		LOGGER.debug("MilkBrandService.getAllAvailableMilkBrands() --- START");
		List<MilkBrandEntity> listAllAvailableMilkBrandsEntities =
				milkBrandRepository.findByMilkBrandAvailableOrderByMilkBrandNameAscPackagingAsc("Y");
		LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsEntities: " + listAllAvailableMilkBrandsEntities);
		if(listAllAvailableMilkBrandsEntities == null || listAllAvailableMilkBrandsEntities.size() == 0) {
			LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- all milk brands not available, raising MilkBrandsNotAvailableException");
			throw new MilkBrandsNotAvailableException();
		}
		List<MilkBrandBean> listAllAvailableMilkBrandsBeans = listAllAvailableMilkBrandsEntities.stream()
				.map(milkBrandEntity -> MilkBrandBean.builder()
						.milkBrandId(milkBrandEntity.getMilkBrandId())
						.milkBrandName(milkBrandEntity.getMilkBrandName())
						.packaging(milkBrandEntity.getPackaging())
						.milkBrandAvailable(milkBrandEntity.getMilkBrandAvailable())
						.build())
				.collect(Collectors.toList());
		LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsBeans: " + listAllAvailableMilkBrandsBeans);
		LOGGER.debug("MilkBrandService.getAllAvailableMilkBrands() --- END");
		return listAllAvailableMilkBrandsBeans;
	}

	public MilkBrandBean saveMilkBrand(int dairyProductId, MilkBrandBean milkBrandBean) {
		LOGGER.debug("MilkBrandService.saveMilkBrand() --- START");
		LOGGER.info("MilkBrandService.saveMilkBrand() --- dairyProductId: " + dairyProductId);
		LOGGER.info("MilkBrandService.saveMilkBrand() --- milkBrandBean to save: " + milkBrandBean);
		LOGGER.info("MilkBrandService.saveMilkBrand() --- finding Milk Brand with same information exists...");
		Optional<MilkBrandEntity> optionalSameNameSamePackagingMilkBrandEntity =
				milkBrandRepository.findByMilkBrandNameAndPackaging(milkBrandBean.getMilkBrandName(),
						milkBrandBean.getPackaging());
		if(optionalSameNameSamePackagingMilkBrandEntity.isPresent()) {
			String milkBrandExistsMessageToLog = "MilkBrandService.saveMilkBrand() --- Milk Brand: " +
					milkBrandBean.getMilkBrandName() +
					", Packaging: " + milkBrandBean.getPackaging() +
					" already exists, raising MilkBrandAlreadyExistsException";
			LOGGER.info("MilkBrandService.saveMilkBrand() --- " + milkBrandExistsMessageToLog);
			String responseMilkBrandExistsMessage = "Milk Brand: " + milkBrandBean.getMilkBrandName() +
					", Packaging: " + milkBrandBean.getPackaging() +
					" already exists.";
			throw new MilkBrandAlreadyExistsException(responseMilkBrandExistsMessage);
		}
		LOGGER.info("MilkBrandService.saveMilkBrand() --- finding Dairy Product Entity with id " +
				dairyProductId + " to attach the given milk brand...");
		DairyProductEntity dairyProductEntity = dairyProductRepository.findById(dairyProductId)
				.orElseThrow(() -> new DairyProductNotAvailableException("Dairy Product with id " + dairyProductId +
						" not found"));
		LOGGER.info("MilkBrandService.saveMilkBrand() --- found Dairy Product Entity: " + dairyProductEntity);
		MilkBrandEntity milkBrandEntityToSave = MilkBrandEntity.builder()
				.milkBrandName(milkBrandBean.getMilkBrandName())
				.packaging(milkBrandBean.getPackaging())
				.milkBrandAvailable(milkBrandBean.getMilkBrandAvailable())
				.dairyProductEntity(dairyProductEntity)
				.build();
		LOGGER.info("MilkBrandService.saveMilkBrand() --- milkBrandEntityToSave to save: " + milkBrandEntityToSave);
		MilkBrandEntity savedMilkBrandEntity = milkBrandRepository.save(milkBrandEntityToSave);
		LOGGER.info("MilkBrandService.saveMilkBrand() --- saved savedMilkBrandEntity: " + savedMilkBrandEntity);
		MilkBrandBean savedMilkBrandBean = MilkBrandBean.builder()
				.milkBrandId(savedMilkBrandEntity.getMilkBrandId())
				.milkBrandName(savedMilkBrandEntity.getMilkBrandName())
				.packaging(savedMilkBrandEntity.getPackaging())
				.milkBrandAvailable(savedMilkBrandEntity.getMilkBrandAvailable())
				.build();
		LOGGER.info("MilkBrandService.saveMilkBrand() --- saved savedMilkBrandBean: " + savedMilkBrandBean);
		LOGGER.debug("MilkBrandService.saveMilkBrand() --- END");
		return savedMilkBrandBean;
	}

	public MilkBrandBean getMilkBrandById(int milkBrandId) {
		LOGGER.debug("MilkBrandService.getMilkBrandById() --- START");
		LOGGER.info("MilkBrandService.getMilkBrandById() --- milkBrandId: " + milkBrandId);
		MilkBrandEntity milkBrandEntity = milkBrandRepository.findById(milkBrandId)
				.orElseThrow(() -> new MilkBrandNotAvailableException("Milk Brand with id " + milkBrandId + " not found"));
		LOGGER.info("MilkBrandService.getMilkBrandById() --- Milk Brand entity with id: " + milkBrandId +
				" found, Milk Brand entity is: " + milkBrandEntity);
		MilkBrandBean milkBrandBean = MilkBrandBean.builder()
				.milkBrandId(milkBrandEntity.getMilkBrandId())
				.milkBrandName(milkBrandEntity.getMilkBrandName())
				.packaging(milkBrandEntity.getPackaging())
				.milkBrandAvailable(milkBrandEntity.getMilkBrandAvailable())
				.build();
		LOGGER.info("MilkBrandService.getMilkBrandById() --- milkBrandBean: " + milkBrandBean);
		LOGGER.debug("MilkBrandService.getMilkBrandById() --- END");
		return milkBrandBean;
	}
}
