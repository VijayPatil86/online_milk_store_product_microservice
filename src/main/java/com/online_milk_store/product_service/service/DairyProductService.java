package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.DairyProductBean;
import com.online_milk_store.product_service.entity.CategoryEntity;
import com.online_milk_store.product_service.entity.DairyProductEntity;
import com.online_milk_store.product_service.exception.CategoryNotAvailableException;
import com.online_milk_store.product_service.exception.DairyProductAlreadyExistsException;
import com.online_milk_store.product_service.exception.DairyProductNotAvailableException;
import com.online_milk_store.product_service.exception.DairyProductsNotAvailableException;
import com.online_milk_store.product_service.repository.CategoryRepository;
import com.online_milk_store.product_service.repository.DairyProductRepository;

@Service
@Transactional
public class DairyProductService {
	static final private Logger LOGGER = LogManager.getLogger(DairyProductService.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private DairyProductRepository dairyProductRepository;

	@Transactional(readOnly = true)
	public List<DairyProductBean> getAllAvailableDairyProducts() {
		LOGGER.debug("DairyProductService.getAllAvailableDairyProducts() --- START");
		List<DairyProductEntity> listAllAvailableDairyProductsEntities = dairyProductRepository.findByDairyProductAvailableIs("Y");
		LOGGER.info("DairyProductService.getAllAvailableDairyProducts() --- listAllAvailableDairyProductsEntities: " + listAllAvailableDairyProductsEntities);
		if(listAllAvailableDairyProductsEntities == null || listAllAvailableDairyProductsEntities.size() == 0) {
			LOGGER.info("DairyProductService.getAllAvailableDairyProducts() --- all dairy products not available, raising DairyProductsNotAvailableException");
			throw new DairyProductsNotAvailableException();
		}
		List<DairyProductBean> listAllAvailableDairyProductsBeans = listAllAvailableDairyProductsEntities.stream()
				.map(dairyProductEntity -> DairyProductBean.builder()
						.dairyProductId(dairyProductEntity.getDairyProductId())
						.dairyProductName(dairyProductEntity.getDairyProductName())
						.dairyProductAvailable(dairyProductEntity.getDairyProductAvailable())
						.build())
				.collect(Collectors.toList());
		LOGGER.info("DairyProductService.getAllAvailableDairyProducts() --- listAllAvailableDairyProductsBeans: " + listAllAvailableDairyProductsBeans);
		LOGGER.debug("DairyProductService.getAllAvailableDairyProducts() --- END");
		return listAllAvailableDairyProductsBeans;
	}

	public DairyProductBean saveDairyProduct(int categoryId, DairyProductBean dairyProductBean) {
		LOGGER.debug("DairyProductService.saveDairyProduct() --- START");
		LOGGER.info("DairyProductService.saveDairyProduct() --- categoryId: " + categoryId);
		LOGGER.info("DairyProductService.saveDairyProduct() --- dairyProductBean to save: " + dairyProductBean);
		LOGGER.info("DairyProductService.saveDairyProduct() --- finding Dairy Product with same name exists...");
		Optional<DairyProductEntity> optionalSameNameDairyProductEntity = dairyProductRepository.findByDairyProductNameIs(dairyProductBean.getDairyProductName());
		if(optionalSameNameDairyProductEntity.isPresent()) {
			LOGGER.info("DairyProductService.saveDairyProduct() --- Dairy Product "
					.concat(dairyProductBean.getDairyProductName())
					.concat(" already exists,")
					.concat(" raising DairyProductAlreadyExistsException"));
			throw new DairyProductAlreadyExistsException("Dairy Product ".concat(dairyProductBean.getDairyProductName()).concat(" already exists."));
		}
		LOGGER.info("DairyProductService.saveDairyProduct() --- finding Category Entity with id " + categoryId + " to attach the given dairy product...");
		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotAvailableException("Category with id " + categoryId + " not found"));
		LOGGER.info("DairyProductService.saveDairyProduct() --- found Category Entity: " + categoryEntity);
		DairyProductEntity dairyProductEntityToSave = DairyProductEntity.builder()
				.dairyProductName(dairyProductBean.getDairyProductName())
				.dairyProductAvailable("Y")
				.categoryEntity(categoryEntity)
				.build();
		LOGGER.info("DairyProductService.saveDairyProduct() --- dairyProductEntityToSave to save: " + dairyProductEntityToSave);
		DairyProductEntity savedDairyProductEntity = dairyProductRepository.save(dairyProductEntityToSave);
		LOGGER.info("DairyProductService.saveDairyProduct() --- saved savedDairyProductEntity: " + savedDairyProductEntity);
		DairyProductBean savedDairyProductBean = DairyProductBean.builder()
				.dairyProductId(savedDairyProductEntity.getDairyProductId())
				.dairyProductName(savedDairyProductEntity.getDairyProductName())
				.dairyProductAvailable(savedDairyProductEntity.getDairyProductAvailable())
				.build();
		LOGGER.info("DairyProductService.saveDairyProduct() --- saved savedDairyProductBean: " + savedDairyProductBean);
		LOGGER.debug("DairyProductService.saveDairyProduct() --- END");
		return savedDairyProductBean;
	}

	public DairyProductBean getDairyProductById(int dairyProductId) {
		LOGGER.debug("DairyProductService.getDairyProductById() --- START");
		LOGGER.info("DairyProductService.getDairyProductById() --- dairyProductId: " + dairyProductId);
		DairyProductEntity dairyProductEntity = dairyProductRepository.findById(dairyProductId)
				.orElseThrow(() -> new DairyProductNotAvailableException("Dairy Product with id " + dairyProductId + " not found"));
		LOGGER.info("DairyProductService.getDairyProductById() --- Dairy Product entity with id: " + dairyProductId + " found, Dairy Product entity is: " + dairyProductEntity);
		DairyProductBean dairyProductBean = DairyProductBean.builder()
				.dairyProductId(dairyProductEntity.getDairyProductId())
				.dairyProductName(dairyProductEntity.getDairyProductName())
				.dairyProductAvailable(dairyProductEntity.getDairyProductAvailable())
				.build();
		LOGGER.info("DairyProductService.getDairyProductById() --- dairyProductBean: " + dairyProductBean);
		LOGGER.debug("DairyProductService.getDairyProductById() --- END");
		return dairyProductBean;
	}
}
