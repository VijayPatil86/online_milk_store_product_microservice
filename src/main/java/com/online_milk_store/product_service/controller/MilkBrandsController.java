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

import com.online_milk_store.product_service.bean.MilkBrandBean;
import com.online_milk_store.product_service.bean.MilkBrandsContainer;
import com.online_milk_store.product_service.exception.MilkBrandsNotAvailableException;
import com.online_milk_store.product_service.service.MilkBrandService;

@CrossOrigin
@RestController
@RequestMapping("/milk-brands")
public class MilkBrandsController {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandsController.class);

	@Autowired
	private MilkBrandService milkBrandService;

	@GetMapping
	public ResponseEntity<MilkBrandsContainer> getAllAvailableMilkBrands() {
		LOGGER.debug("MilkBrandsController.getAllAvailableMilkBrands() --- START");
		List<MilkBrandBean> listAllAvailableMilkBrandsBeans = milkBrandService.getAllAvailableMilkBrands();
		LOGGER.info("MilkBrandsController.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsBeans: " + listAllAvailableMilkBrandsBeans);
		MilkBrandsContainer milkBrandsContainer = MilkBrandsContainer.builder()
				.milkBrandBeans(listAllAvailableMilkBrandsBeans)
				.build();
		milkBrandsContainer.add(
				WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class).getAllAvailableMilkBrands()).withRel("link_getAllAvailableMilkBrands")
			);
		LOGGER.info("MilkBrandsController.getAllAvailableMilkBrands() --- milkBrandsContainer: " + milkBrandsContainer);
		LOGGER.debug("MilkBrandsController.getAllAvailableMilkBrands() --- END");
		return new ResponseEntity<>(milkBrandsContainer, HttpStatus.OK);
	}

	/** Exception Handling section **/
	@ExceptionHandler(value = {MilkBrandsNotAvailableException.class})
	public ResponseEntity<Void> handleMilkBrandsNotAvailableException() {
		LOGGER.debug("MilkBrandsController.handleMilkBrandsNotAvailableException() --- START");
		LOGGER.info("MilkBrandsController.handleMilkBrandsNotAvailableException() --- Milk Brands Not Available, returning No Content 204");
		LOGGER.debug("MilkBrandsController.handleMilkBrandsNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}
}
