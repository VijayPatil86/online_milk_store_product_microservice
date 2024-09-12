package com.online_milk_store.product_service.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.CategoryBean;
import com.online_milk_store.product_service.exception.CategoriesNotAvailableException;
import com.online_milk_store.product_service.exception.CategoryAlreadyExistsException;
import com.online_milk_store.product_service.service.CategoryService;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoriesController {
	static final private Logger LOGGER = LogManager.getLogger(CategoriesController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<CategoryBean> getAllAvailableCategories() {
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- START");
		List<CategoryBean> listAllAvailableCategoriesBeans = categoryService.getAllAvailableCategories();
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- listAllAvailableCategoryBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- END");
		return listAllAvailableCategoriesBeans;
	}
	
	@PostMapping
	public List<CategoryBean> createCategory(@RequestBody CategoryBean categoryBean){
		LOGGER.debug("CategoriesController.createCategory() --- START");
		LOGGER.info("CategoriesController.createCategory() --- Category to create:" + categoryBean);
		List<CategoryBean> listAllAvailableCategoriesBeans = categoryService.createCategory(categoryBean);
		LOGGER.info("CategoryService.createCategory() --- listAllAvailableCategoriesBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.debug("CategoriesController.createCategory() --- END");
		return listAllAvailableCategoriesBeans;
	}
	
	@ExceptionHandler(value = {CategoriesNotAvailableException.class})
	public ResponseEntity<Void> handleCategoriesNotAvailableException() {
		LOGGER.debug("CategoriesController.handleCategoriesNotAvailableException() --- START");
		LOGGER.info("CategoriesController.handleCategoriesNotAvailableException() --- Categories Not Available, returning No Content 204");
		LOGGER.debug("CategoriesController.handleCategoriesNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}
	
	@ExceptionHandler(value = {CategoryAlreadyExistsException.class})
	public ResponseEntity<String> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException categoryAlreadyExistsException) {
		LOGGER.debug("CategoriesController.handleCategoryAlreadyExistsException() --- START");
		LOGGER.info("CategoriesController.handleCategoryAlreadyExistsException() --- " + categoryAlreadyExistsException.getMessage() + ", returning Conflict 409");
		LOGGER.debug("CategoriesController.handleCategoryAlreadyExistsException() --- END");
		return new ResponseEntity<>(categoryAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
	}
}
