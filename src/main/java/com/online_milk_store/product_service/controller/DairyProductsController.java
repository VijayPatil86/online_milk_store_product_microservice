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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.DairyProductBean;
import com.online_milk_store.product_service.bean.DairyProductsContainer;
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
	public ResponseEntity<List<DairyProductBean>> getAllAvailableDairyProducts() {
		LOGGER.debug("DairyProductsController.getAllAvailableDairyProducts() --- START");
		List<DairyProductBean> listAllAvailableDairyProductsBeans = dairyProductService.getAllAvailableDairyProducts();
		LOGGER.info("DairyProductsController.getAllAvailableDairyProducts() --- listAllAvailableDairyProductsBeans: " + listAllAvailableDairyProductsBeans);
		ResponseEntity<List<DairyProductBean>> responseEntity = new ResponseEntity<>(listAllAvailableDairyProductsBeans, HttpStatus.OK);
		LOGGER.debug("DairyProductsController.getAllAvailableDairyProducts() --- END");
		return responseEntity;
	}

	/** Exception Handling section **/
	@ExceptionHandler(value = {DairyProductsNotAvailableException.class})
	public ResponseEntity<Void> handleDairyProductsNotAvailableException() {
		LOGGER.debug("DairyProductsController.handleDairyProductsNotAvailableException() --- START");
		LOGGER.info("DairyProductsController.handleDairyProductsNotAvailableException() --- Dairy Products Not Available, returning No Content 204");
		LOGGER.debug("DairyProductsController.handleDairyProductsNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}
}
