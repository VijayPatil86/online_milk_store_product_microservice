package com.online_milk_store.product_service.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.CategoryBean;
import com.online_milk_store.product_service.bean.CategoryContainer;
import com.online_milk_store.product_service.exception.CategoriesNotAvailableException;
import com.online_milk_store.product_service.exception.CategoryAlreadyExistsException;
import com.online_milk_store.product_service.exception.CategoryNotAvailableException;
import com.online_milk_store.product_service.service.CategoryService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoriesController {
	static final private Logger LOGGER = LogManager.getLogger(CategoriesController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<CategoryContainer> getAllAvailableCategories() {
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- START");
		List<CategoryBean> listAllAvailableCategoriesBeans = categoryService.getAllAvailableCategories();
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- listAllAvailableCategoryBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- adding HATEOAS links");
		CategoryContainer categoryContainer = CategoryContainer.builder()
				.categoryBeans(listAllAvailableCategoriesBeans)
				.build();
		categoryContainer = hateoas_getAllAvailableCategories(categoryContainer);
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- HATEOAS links are: " + categoryContainer);
		ResponseEntity<CategoryContainer> responseEntity = new ResponseEntity<>(categoryContainer, HttpStatus.OK);
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- END");
		return responseEntity;
	}
	
	private CategoryContainer hateoas_getAllAvailableCategories(CategoryContainer categoryContainer) {
		LOGGER.debug("CategoriesController.hateoas_getAllAvailableCategories() --- START");
		categoryContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories()).withSelfRel());
		LOGGER.info("CategoriesController.hateoas_getAllAvailableCategories() --- HATEOAS links are: " + categoryContainer);
		LOGGER.debug("CategoriesController.hateoas_getAllAvailableCategories() --- END");
		return categoryContainer;
	}

	@PostMapping
	public ResponseEntity<List<CategoryBean>> createCategory(@RequestBody CategoryBean categoryBean){
		LOGGER.debug("CategoriesController.createCategory() --- START");
		LOGGER.info("CategoriesController.createCategory() --- Category to create:" + categoryBean);
		List<CategoryBean> listAllAvailableCategoriesBeans = categoryService.createCategory(categoryBean);
		LOGGER.info("CategoryService.createCategory() --- listAllAvailableCategoriesBeans: " + listAllAvailableCategoriesBeans);
		ResponseEntity<List<CategoryBean>> responseEntity = new ResponseEntity<>(listAllAvailableCategoriesBeans, HttpStatus.OK);
		LOGGER.debug("CategoriesController.createCategory() --- END");
		return responseEntity;
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryBean> updateCategory(@PathVariable("categoryId") Integer categoryId, @RequestBody CategoryBean categoryBean){
		LOGGER.debug("CategoriesController.updateCategory() --- START");
		LOGGER.info("CategoriesController.updateCategory() --- id of Category to update:" + categoryId);
		LOGGER.info("CategoriesController.updateCategory() --- CategoryBean:" + categoryBean);
		categoryBean = categoryService.updateCategory(categoryId, categoryBean);
		LOGGER.info("CategoriesController.updateCategory() --- updated Category:" + categoryBean);
		LOGGER.debug("CategoriesController.updateCategory() --- END");
		return new ResponseEntity<>(categoryBean, HttpStatus.OK);
	}

	/*** Exception Handling section ***/
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

	@ExceptionHandler(value = {CategoryNotAvailableException.class})
	public ResponseEntity<String> handleCategoryNotAvailableException(CategoryNotAvailableException categoryNotAvailableException) {
		LOGGER.debug("CategoriesController.handleCategoryNotAvailableException() --- START");
		LOGGER.info("CategoriesController.handleCategoryNotAvailableException() --- " + categoryNotAvailableException.getMessage() + ", returning No Content 204");
		LOGGER.debug("CategoriesController.handleCategoryNotAvailableException() --- END");
		return new ResponseEntity<>(categoryNotAvailableException.getMessage(), HttpStatus.NO_CONTENT);
	}
}
