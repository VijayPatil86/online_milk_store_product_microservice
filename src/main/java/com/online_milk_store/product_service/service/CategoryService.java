package com.online_milk_store.product_service.service;

import java.util.List;
import java.util.Objects;
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
import com.online_milk_store.product_service.exception.CategoryNotAvailableException;
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
		LOGGER.info("CategoryService.getAllAvailableCategories() --- listAllAvailableCategoriesEntities: " + listAllAvailableCategoriesEntities);
		if(listAllAvailableCategoriesEntities == null || listAllAvailableCategoriesEntities.size() == 0) {
			LOGGER.info("CategoryService.getAllAvailableCategories() --- all categories not available, raising CategoriesNotAvailableException");
			throw new CategoriesNotAvailableException();
		}
		List<CategoryBean> listAllAvailableCategoriesBeans = listAllAvailableCategoriesEntities.stream()
			.map(categoryEntity -> CategoryBean.builder()
					.categoryId(categoryEntity.getCategoryId())
					.categoryName(categoryEntity.getCategoryName())
					.categoryAvailable(categoryEntity.getCategoryAvailable())
					.build())
			.collect(Collectors.toList());
		LOGGER.info("CategoryService.getAllAvailableCategories() --- listAllAvailableCategoriesBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.debug("CategoryService.getAllAvailableCategories() --- END");
		return listAllAvailableCategoriesBeans;
	}

	public CategoryBean createCategory(CategoryBean categoryBean) {
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
			.categoryAvailable(categoryBean.getCategoryAvailable())
			.build();
		LOGGER.info("CategoryService.createCategory() --- CategoryEntity to save: " + categoryEntityToSave);
		CategoryEntity categoryEntitySaved = categoryRepository.save(categoryEntityToSave);
		LOGGER.info("CategoryService.createCategory() --- saved CategoryEntity: " + categoryEntitySaved);
		categoryBean.setCategoryId(categoryEntitySaved.getCategoryId());
		categoryBean.setCategoryName(categoryEntitySaved.getCategoryName());
		categoryBean.setCategoryAvailable(categoryEntitySaved.getCategoryAvailable());
		LOGGER.info("CategoryService.createCategory() --- saved CategoryBean: " + categoryBean);
		LOGGER.debug("CategoryService.createCategory() --- END");
		LOGGER.debug("CategoryService.createCategory() --- retrieving all available categories");
		return categoryBean;
	}

	public CategoryBean updateCategory(int categoryId, CategoryBean categoryBean) {
		LOGGER.debug("CategoryService.updateCategory() --- START");
		LOGGER.info("CategoryService.updateCategory() --- id of Category to update: " + categoryId);
		LOGGER.info("CategoryService.updateCategory() --- Category to update: " + categoryBean);
		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotAvailableException("Category with id " + categoryId + " not found"));
		categoryEntity.setCategoryName(categoryBean.getCategoryName());
		categoryEntity.setCategoryAvailable(Objects.isNull(categoryBean.getCategoryAvailable()) ? "Y" : categoryBean.getCategoryAvailable());
		CategoryEntity categoryEntityUpdated = categoryRepository.save(categoryEntity);
		LOGGER.info("CategoryService.updateCategory() --- updated category entity: " + categoryEntityUpdated);
		categoryBean.setCategoryId(categoryEntity.getCategoryId());
		categoryBean.setCategoryName(categoryEntity.getCategoryName());
		categoryBean.setCategoryAvailable(categoryEntity.getCategoryAvailable());
		LOGGER.info("CategoryService.updateCategory() --- updated category bean: " + categoryBean);
		LOGGER.debug("CategoryService.updateCategory() --- END");
		return categoryBean;
	}

	public CategoryBean getCategoryById(int categoryId) {
		LOGGER.debug("CategoryService.getCategoryById() --- START");
		LOGGER.info("CategoryService.getCategoryById() --- categoryId: " + categoryId);
		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotAvailableException("Category with id " + categoryId + " not found"));
		LOGGER.info("CategoryService.getCategoryById() --- category entity with category id: " + categoryId + " found, category entity is: " + categoryEntity);
		CategoryBean categoryBean = CategoryBean.builder()
				.categoryId(categoryEntity.getCategoryId())
				.categoryName(categoryEntity.getCategoryName())
				.categoryAvailable(categoryEntity.getCategoryAvailable())
				.build();
		LOGGER.info("CategoryService.getCategoryById() --- categoryBean: " + categoryBean);
		LOGGER.debug("CategoryService.getCategoryById() --- END");
		return categoryBean;
	}
}
