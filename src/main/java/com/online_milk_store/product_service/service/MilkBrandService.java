package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.MilkBrandBean;
import com.online_milk_store.product_service.entity.MilkBrandEntity;
import com.online_milk_store.product_service.exception.MilkBrandsNotAvailableException;
import com.online_milk_store.product_service.repository.MilkBrandRepository;

@Service
@Transactional
public class MilkBrandService {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandService.class);

	@Autowired
	private MilkBrandRepository milkBrandRepository;

	@Transactional(readOnly = true)
	public List<MilkBrandBean> getAllAvailableMilkBrands() {
		LOGGER.debug("MilkBrandService.getAllAvailableMilkBrands() --- START");
		List<MilkBrandEntity> listAllAvailableMilkBrandsEntities = milkBrandRepository.findByMilkBrandAvailableIs("Y");
		LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsEntities: " + listAllAvailableMilkBrandsEntities);
		if(listAllAvailableMilkBrandsEntities == null || listAllAvailableMilkBrandsEntities.size() == 0) {
			LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- all milk brands not available, raising MilkBrandsNotAvailableException");
			throw new MilkBrandsNotAvailableException();
		}
		List<MilkBrandBean> listAllAvailableMilkBrandsBeans = listAllAvailableMilkBrandsEntities.stream()
				.map(milkBrandEntity -> MilkBrandBean.builder()
						.milkBrandId(milkBrandEntity.getMilkBrandId())
						.milkBrandName(milkBrandEntity.getMilkBrandName())
						.milkBrandAvailable(milkBrandEntity.getMilkBrandAvailable())
						.build())
				.collect(Collectors.toList());
		LOGGER.info("MilkBrandService.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsBeans: " + listAllAvailableMilkBrandsBeans);
		LOGGER.debug("MilkBrandService.getAllAvailableMilkBrands() --- END");
		return listAllAvailableMilkBrandsBeans;
	}
}
