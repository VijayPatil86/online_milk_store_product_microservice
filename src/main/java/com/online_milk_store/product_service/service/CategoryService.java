package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_milk_store.product_service.bean.CategoryBean;
import com.online_milk_store.product_service.entity.CategoryEntity;
import com.online_milk_store.product_service.exception.CategoriesNotAvailableException;
import com.online_milk_store.product_service.exception.CategoryAlreadyExistsException;
import com.online_milk_store.product_service.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {
	static final private Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryBean> getAllAvailableCategories() {
		LOGGER.debug("CategoryService.getAllAvailableCategories() --- START");
		List<CategoryEntity> listAllAvailableCategoriesEntities = categoryRepository.findByCategoryAvailableIs("Y");
		if(listAllAvailableCategoriesEntities == null || listAllAvailableCategoriesEntities.size() == 0) {
			LOGGER.info("CategoryService.getAllAvailableCategories() --- all categories not available, raising CategoriesNotAvailableException");
			throw new CategoriesNotAvailableException();
		}
		LOGGER.info("CategoryService.getAllAvailableCategories() --- listAllAvailableCategoriesEntities: " + listAllAvailableCategoriesEntities);
		List<CategoryBean> listAllAvailableCategoriesBeans = listAllAvailableCategoriesEntities.stream()
			.map(categoryEntity -> CategoryBean.builder()
					.categoryId(categoryEntity.getCategoryId())
					.categoryName(categoryEntity.getCategoryName())
					.build())
			.collect(Collectors.toList());
		LOGGER.info("CategoryService.getAllAvailableCategories() --- listAllAvailableCategoriesBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.debug("CategoryService.getAllAvailableCategories() --- END");
		return listAllAvailableCategoriesBeans;
	}

	public List<CategoryBean> createCategory(CategoryBean categoryBean) {
		LOGGER.debug("CategoryService.createCategory() --- START");
		LOGGER.debug("CategoryService.createCategory() --- Category to create: " + categoryBean);
		Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findByCategoryNameIs(categoryBean.getCategoryName());
		if(optionalCategoryEntity.isPresent()) {
			LOGGER.info("CategoryService.createCategory() --- Category "
					.concat(categoryBean.getCategoryName())
					.concat(" already exists,")
					.concat(" raising CategoryAlreadyExistsException"));
			throw new CategoryAlreadyExistsException("Category ".concat(categoryBean.getCategoryName()).concat(" already exists."));
		}
		CategoryEntity categoryEntityToSave = CategoryEntity.builder()
			.categoryName(categoryBean.getCategoryName())
			.build();
		LOGGER.info("CategoryService.createCategory() --- CategoryEntity to save: " + categoryEntityToSave);
		CategoryEntity categoryEntitySaved = categoryRepository.save(categoryEntityToSave);
		LOGGER.info("CategoryService.createCategory() --- saved CategoryEntity: " + categoryEntitySaved);
		LOGGER.debug("CategoryService.createCategory() --- END");
		LOGGER.debug("CategoryService.createCategory() --- retrieving all available categories");
		return getAllAvailableCategories();
	}
}
