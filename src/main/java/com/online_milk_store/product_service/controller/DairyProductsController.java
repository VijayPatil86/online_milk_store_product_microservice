package com.online_milk_store.product_service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.DairyProductBean;
import com.online_milk_store.product_service.bean.DairyProductsContainer;
import com.online_milk_store.product_service.exception.CategoryNotAvailableException;
import com.online_milk_store.product_service.exception.DairyProductAlreadyExistsException;
import com.online_milk_store.product_service.exception.DairyProductNotAvailableException;
import com.online_milk_store.product_service.exception.DairyProductsNotAvailableException;
import com.online_milk_store.product_service.service.DairyProductService;

@CrossOrigin
@RestController
@RequestMapping("/dairy-products")
public class DairyProductsController {
	static final private Logger LOGGER = LogManager.getLogger(DairyProductsController.class);

	@Autowired
	private DairyProductService dairyProductService;

	@GetMapping("/get_HATEOAS_links")
	public ResponseEntity<DairyProductsContainer> get_HATEOAS_links() {
		LOGGER.debug("DairyProductsController.get_HATEOAS_links() --- START");
		DairyProductsContainer dairyProductsContainer = DairyProductsContainer.builder().build();
		dairyProductsContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getAllAvailableDairyProducts()).withRel("link_getAllAvailableDairyProducts")
		);
		LOGGER.info("DairyProductsController.get_HATEOAS_links() --- dairyProductsContainer: " + dairyProductsContainer);
		LOGGER.debug("DairyProductsController.get_HATEOAS_links() --- END");
		return new ResponseEntity<>(dairyProductsContainer, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<DairyProductsContainer> getAllAvailableDairyProducts() {
		LOGGER.debug("DairyProductsController.getAllAvailableDairyProducts() --- START");
		List<DairyProductBean> listAllAvailableDairyProductsBeans = dairyProductService.getAllAvailableDairyProducts();
		listAllAvailableDairyProductsBeans.stream()
			.forEach(dairyProductBean -> dairyProductBean.add(
					WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getDairyProductById(dairyProductBean.getDairyProductId())).withRel("link_getDairyProductById")));
		LOGGER.info("DairyProductsController.getAllAvailableDairyProducts() --- listAllAvailableDairyProductsBeans with HATEOAS link: " + listAllAvailableDairyProductsBeans);
		DairyProductsContainer dairyProductsContainer = DairyProductsContainer.builder()
				.dairyProductBeans(listAllAvailableDairyProductsBeans)
				.build();
		dairyProductsContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getAllAvailableDairyProducts()).withRel("link_getAllAvailableDairyProducts"));
		LOGGER.info("DairyProductsController.getAllAvailableDairyProducts() --- dairyProductsContainer: " + dairyProductsContainer);
		ResponseEntity<DairyProductsContainer> responseEntity = new ResponseEntity<>(dairyProductsContainer, HttpStatus.OK);
		LOGGER.debug("DairyProductsController.getAllAvailableDairyProducts() --- END");
		return responseEntity;
	}

	@PostMapping("/{categoryId}")
	public ResponseEntity<DairyProductsContainer> saveDairyProduct(@PathVariable(name = "categoryId") int categoryId, @RequestBody DairyProductBean dairyProductBean) {
		LOGGER.debug("DairyProductsController.saveDairyProduct() --- START");
		LOGGER.info("DairyProductsController.saveDairyProduct() --- categoryId: " + categoryId);
		LOGGER.info("DairyProductsController.saveDairyProduct() --- dairyProductBean to save: " + dairyProductBean);
		DairyProductBean savedDairyProductBean = dairyProductService.saveDairyProduct(categoryId, dairyProductBean);
		LOGGER.info("DairyProductsController.saveDairyProduct() --- saved dairyProductBean: " + savedDairyProductBean);
		savedDairyProductBean.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getDairyProductById(savedDairyProductBean.getDairyProductId())).withRel("link_getDairyProductById"));
		LOGGER.info("DairyProductsController.saveDairyProduct() --- HATEOAS links to dairyProductBean are: " + savedDairyProductBean);
		DairyProductsContainer dairyProductsContainer = DairyProductsContainer.builder().build();
		dairyProductsContainer.setDairyProductBean(savedDairyProductBean);
		dairyProductsContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getAllAvailableDairyProducts()).withRel("link_getAllAvailableDairyProducts"));
		LOGGER.info("DairyProductsController.saveDairyProduct() --- dairyProductsContainer: " + dairyProductsContainer);
		LOGGER.debug("DairyProductsController.saveDairyProduct() --- END");
		return new ResponseEntity<>(dairyProductsContainer, HttpStatus.OK);
	}

	@GetMapping("/{dairyProductId}")
	public ResponseEntity<DairyProductsContainer> getDairyProductById(@PathVariable(name = "dairyProductId") int dairyProductId) {
		LOGGER.debug("DairyProductsController.getDairyProductById() --- START");
		LOGGER.info("DairyProductsController.getDairyProductById() --- dairyProductId: " + dairyProductId);
		DairyProductBean dairyProductBean = dairyProductService.getDairyProductById(dairyProductId);
		dairyProductBean.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getDairyProductById(dairyProductId)).withRel("link_getDairyProductById"));
		LOGGER.info("DairyProductsController.getDairyProductById() --- dairyProductBean: " + dairyProductBean);
		DairyProductsContainer dairyProductsContainer = DairyProductsContainer.builder()
				.dairyProductBean(dairyProductBean)
				.build();
		dairyProductsContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(DairyProductsController.class).getAllAvailableDairyProducts()).withRel("link_getAllAvailableDairyProducts"));
		LOGGER.info("DairyProductsController.getDairyProductById() --- dairyProductsContainer: " + dairyProductsContainer);
		return new ResponseEntity<>(dairyProductsContainer, HttpStatus.OK);
	}

	/** Exception Handling section **/
	@ExceptionHandler(value = {DairyProductsNotAvailableException.class})
	public ResponseEntity<Void> handleDairyProductsNotAvailableException() {
		LOGGER.debug("DairyProductsController.handleDairyProductsNotAvailableException() --- START");
		LOGGER.info("DairyProductsController.handleDairyProductsNotAvailableException() --- Dairy Products Not Available, returning No Content 204");
		LOGGER.debug("DairyProductsController.handleDairyProductsNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(value = {CategoryNotAvailableException.class})
	public ResponseEntity<String> handleCategoryNotAvailableException(CategoryNotAvailableException categoryNotAvailableException) {
		LOGGER.debug("DairyProductsController.handleCategoryNotAvailableException() --- START");
		LOGGER.info("DairyProductsController.handleCategoryNotAvailableException() --- " + categoryNotAvailableException.getMessage() + ", returning No Content 204");
		LOGGER.debug("DairyProductsController.handleCategoryNotAvailableException() --- END");
		return new ResponseEntity<>(categoryNotAvailableException.getMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(value = {DairyProductAlreadyExistsException.class})
	public ResponseEntity<String> handleDairyProductAlreadyExistsException(DairyProductAlreadyExistsException dairyProductAlreadyExistsException) {
		LOGGER.debug("DairyProductsController.handleDairyProductAlreadyExistsException() --- START");
		LOGGER.info("DairyProductsController.handleDairyProductAlreadyExistsException() --- " + dairyProductAlreadyExistsException.getMessage() + ", returning Conflict 409");
		LOGGER.debug("DairyProductsController.handleDairyProductAlreadyExistsException() --- END");
		return new ResponseEntity<>(dairyProductAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = {DairyProductNotAvailableException.class})
	public ResponseEntity<String> handleDairyProductNotAvailableException(DairyProductNotAvailableException dairyProductNotAvailableException) {
		LOGGER.debug("DairyProductsController.handleDairyProductNotAvailableException() --- START");
		LOGGER.info("DairyProductsController.handleDairyProductNotAvailableException() --- " + dairyProductNotAvailableException.getMessage() + ", returning No Content 204");
		LOGGER.debug("DairyProductsController.handleDairyProductNotAvailableException() --- END");
		return new ResponseEntity<>(dairyProductNotAvailableException.getMessage(), HttpStatus.NO_CONTENT);
	}
}
