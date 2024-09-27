package com.online_milk_store.product_service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
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

import com.online_milk_store.product_service.bean.CategoriesContainer;
import com.online_milk_store.product_service.bean.CategoryBean;
import com.online_milk_store.product_service.exception.CategoriesNotAvailableException;
import com.online_milk_store.product_service.exception.CategoryAlreadyExistsException;
import com.online_milk_store.product_service.exception.CategoryNotAvailableException;
import com.online_milk_store.product_service.service.CategoryService;
import com.online_milk_store.product_service.util.Util;

@CrossOrigin
@RestController
@RequestMapping("/product-service/categories")
public class CategoriesController {
	static final private Logger LOGGER = LogManager.getLogger(CategoriesController.class);

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private Util util;

	@GetMapping("/get_HATEOAS_links")
	public ResponseEntity<CategoriesContainer> get_HATEOAS_links() {
		LOGGER.debug("CategoriesController.get_HATEOAS_links() --- START");
		CategoriesContainer categoriesContainer = CategoriesContainer.builder().build();
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories())
				.withRel("link_getAllAvailableCategories")
				.getHref(), "link_getAllAvailableCategories");
		categoriesContainer.add(linkGateway);
		LOGGER.info("CategoriesController.get_HATEOAS_links() --- categoriesContainer: " + categoriesContainer);
		LOGGER.debug("CategoriesController.get_HATEOAS_links() --- END");
		return new ResponseEntity<>(categoriesContainer, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<CategoriesContainer> getAllAvailableCategories() {
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- START");
		List<CategoryBean> listAllAvailableCategoriesBeans = categoryService.getAllAvailableCategories();
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- listAllAvailableCategoryBeans: " + listAllAvailableCategoriesBeans);
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- adding HATEOAS links");
		listAllAvailableCategoriesBeans.stream()
			.forEach(availableCategoriesBean -> {
				Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class)
						.getCategoryById(availableCategoriesBean.getCategoryId()))
						.withRel("link_getCategoryById")
						.getHref(), "link_getCategoryById");
				availableCategoriesBean.add(linkGateway);
			});
		CategoriesContainer categoryContainer = CategoriesContainer.builder()
				.categoryBeans(listAllAvailableCategoriesBeans)
				.build();
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories())
				.withRel("link_getAllAvailableCategories")
				.getHref(), "link_getAllAvailableCategories");
		categoryContainer.add(linkGateway);
		LOGGER.info("CategoriesController.getAllAvailableCategories() --- HATEOAS links are: " + categoryContainer);
		ResponseEntity<CategoriesContainer> responseEntity = new ResponseEntity<>(categoryContainer, HttpStatus.OK);
		LOGGER.debug("CategoriesController.getAllAvailableCategories() --- END");
		return responseEntity;
	}

	@PostMapping
	public ResponseEntity<CategoriesContainer> createCategory(@RequestBody CategoryBean categoryBean){
		LOGGER.debug("CategoriesController.createCategory() --- START");
		LOGGER.info("CategoriesController.createCategory() --- Category to create:" + categoryBean);
		CategoryBean createdCategoryBean = categoryService.createCategory(categoryBean);
		LOGGER.info("CategoryService.createCategory() --- createdCategoryBean: " + createdCategoryBean);
		LOGGER.info("CategoriesController.createCategory() --- adding HATEOAS links");
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class)
				.getCategoryById(createdCategoryBean.getCategoryId()))
				.withRel("link_getCategoryById")
				.getHref(), "link_getCategoryById");
		createdCategoryBean.add(linkGateway);
		CategoriesContainer categoriesContainer = CategoriesContainer.builder()
				.categoryBean(createdCategoryBean)
				.build();
		linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories())
				.withRel("link_getAllAvailableCategories")
				.getHref(), "link_getAllAvailableCategories");
		categoriesContainer.add(linkGateway);
		LOGGER.info("CategoryService.createCategory() --- categoriesContainer: " + categoriesContainer);
		ResponseEntity<CategoriesContainer> responseEntity = new ResponseEntity<>(categoriesContainer, HttpStatus.OK);
		LOGGER.debug("CategoriesController.createCategory() --- END");
		return responseEntity;
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoriesContainer> updateCategory(@PathVariable("categoryId") Integer categoryId, @RequestBody CategoryBean categoryBean){
		LOGGER.debug("CategoriesController.updateCategory() --- START");
		LOGGER.info("CategoriesController.updateCategory() --- id of Category to update:" + categoryId);
		LOGGER.info("CategoriesController.updateCategory() --- CategoryBean:" + categoryBean);
		categoryBean = categoryService.updateCategory(categoryId, categoryBean);
		LOGGER.info("CategoriesController.updateCategory() --- updated Category:" + categoryBean);
		LOGGER.info("CategoriesController.updateCategory() --- adding HATEOAS links");
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getCategoryById(categoryBean.getCategoryId()))
				.withRel("link_getCategoryById")
				.getHref(), "link_getCategoryById");
		categoryBean.add(linkGateway);
		CategoriesContainer categoriesContainer = CategoriesContainer.builder()
				.categoryBean(categoryBean)
				.build();
		linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories())
				.withRel("link_getAllAvailableCategories")
				.getHref(), "link_getAllAvailableCategories");
		categoriesContainer.add(linkGateway);
		LOGGER.info("CategoriesController.updateCategory() --- categoriesContainer: " + categoriesContainer);
		LOGGER.debug("CategoriesController.updateCategory() --- END");
		return new ResponseEntity<>(categoriesContainer, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoriesContainer> getCategoryById(@PathVariable(name = "categoryId") int categoryId){
		LOGGER.debug("CategoriesController.getCategoryById() --- START");
		LOGGER.info("CategoriesController.getCategoryById() --- categoryId: " + categoryId);
		CategoryBean categoryBean = categoryService.getCategoryById(categoryId);
		LOGGER.info("CategoriesController.getCategoryById() --- categoryBean: " + categoryBean);
		LOGGER.info("CategoriesController.getCategoryById() --- adding HATEOAS links");
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getCategoryById(categoryBean.getCategoryId()))
				.withRel("link_getCategoryById")
				.getHref(), "link_getCategoryById");
		categoryBean.add(linkGateway);
		CategoriesContainer categoriesContainer = CategoriesContainer.builder()
				.categoryBean(categoryBean)
				.build();
		linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(CategoriesController.class).getAllAvailableCategories())
				.withRel("link_getAllAvailableCategories")
				.getHref(), "link_getAllAvailableCategories");
		categoriesContainer.add(linkGateway);
		LOGGER.info("CategoriesController.getCategoryById() --- categoriesContainer: " + categoriesContainer);
		LOGGER.debug("CategoriesController.getCategoryById() --- END");
		return new ResponseEntity<>(categoriesContainer, HttpStatus.OK);
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
