package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.DairyProductBean;
import com.online_milk_store.product_service.entity.DairyProductEntity;
import com.online_milk_store.product_service.exception.DairyProductsNotAvailableException;
import com.online_milk_store.product_service.repository.DairyProductRepository;

@Service
@Transactional
public class DairyProductService {
	static final private Logger LOGGER = LogManager.getLogger(DairyProductService.class);

	@Autowired
	private DairyProductRepository dairyProductRepository;

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
}
