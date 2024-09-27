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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.MilkBrandBean;
import com.online_milk_store.product_service.bean.MilkBrandsContainer;
import com.online_milk_store.product_service.exception.DairyProductNotAvailableException;
import com.online_milk_store.product_service.exception.MilkBrandAlreadyExistsException;
import com.online_milk_store.product_service.exception.MilkBrandNotAvailableException;
import com.online_milk_store.product_service.exception.MilkBrandsNotAvailableException;
import com.online_milk_store.product_service.service.MilkBrandService;
import com.online_milk_store.product_service.util.Util;

@CrossOrigin
@RestController
@RequestMapping("/product-service/milk-brands")
public class MilkBrandsController {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandsController.class);

	@Autowired
	private MilkBrandService milkBrandService;

	@Autowired
	private Util util;

	@GetMapping("/get_HATEOAS_links")
	public ResponseEntity<MilkBrandsContainer> get_HATEOAS_links() {
		LOGGER.debug("MilkBrandsController.get_HATEOAS_links() --- START");
		MilkBrandsContainer milkBrandsContainer = MilkBrandsContainer.builder().build();
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getAllAvailableMilkBrands())
			.withRel("link_getAllAvailableMilkBrands")
			.getHref(), "link_getAllAvailableMilkBrands");
		milkBrandsContainer.add(linkGateway);
		LOGGER.debug("MilkBrandsController.get_HATEOAS_links() --- milkBrandsContainer: " + milkBrandsContainer);
		LOGGER.debug("MilkBrandsController.get_HATEOAS_links() --- END");
		return new ResponseEntity<>(milkBrandsContainer, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<MilkBrandsContainer> getAllAvailableMilkBrands() {
		LOGGER.debug("MilkBrandsController.getAllAvailableMilkBrands() --- START");
		List<MilkBrandBean> listAllAvailableMilkBrandsBeans = milkBrandService.getAllAvailableMilkBrands();
		listAllAvailableMilkBrandsBeans.stream()
			.forEach(milkBrandsBean -> {
				Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
						.getMilkBrandById(milkBrandsBean.getMilkBrandId()))
					.withRel("link_getMilkBrandById")
					.getHref(), "link_getMilkBrandById");
				milkBrandsBean.add(linkGateway);
			});
		LOGGER.info("MilkBrandsController.getAllAvailableMilkBrands() --- listAllAvailableMilkBrandsBeans: " + listAllAvailableMilkBrandsBeans);
		MilkBrandsContainer milkBrandsContainer = MilkBrandsContainer.builder()
				.milkBrandBeans(listAllAvailableMilkBrandsBeans)
				.build();
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getAllAvailableMilkBrands())
			.withRel("link_getAllAvailableMilkBrands")
			.getHref(), "link_getAllAvailableMilkBrands");
		milkBrandsContainer.add(linkGateway);
		LOGGER.info("MilkBrandsController.getAllAvailableMilkBrands() --- milkBrandsContainer: " + milkBrandsContainer);
		LOGGER.debug("MilkBrandsController.getAllAvailableMilkBrands() --- END");
		return new ResponseEntity<>(milkBrandsContainer, HttpStatus.OK);
	}

	@PostMapping("/{dairyProductId}")
	public ResponseEntity<MilkBrandsContainer> saveMilkBrand(@PathVariable("dairyProductId") int dairyProductId,
			@RequestBody MilkBrandBean milkBrandBean) {
		LOGGER.debug("MilkBrandsController.saveMilkBrand() --- START");
		LOGGER.info("MilkBrandsController.saveMilkBrand() --- dairyProductId: " + dairyProductId);
		LOGGER.info("MilkBrandsController.saveMilkBrand() --- milkBrandBean to save: " + milkBrandBean);
		MilkBrandBean savedMilkBrandBean = milkBrandService.saveMilkBrand(dairyProductId, milkBrandBean);
		LOGGER.info("MilkBrandsController.saveMilkBrand() --- saved MilkBrandBean: " + savedMilkBrandBean);
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getMilkBrandById(savedMilkBrandBean.getMilkBrandId()))
			.withRel("link_getMilkBrandById")
			.getHref(), "link_getMilkBrandById");
		savedMilkBrandBean.add(linkGateway);
		LOGGER.info("MilkBrandsController.saveMilkBrand() --- added HATEOAS links to MilkBrandBean: " + savedMilkBrandBean);
		MilkBrandsContainer milkBrandsContainer = MilkBrandsContainer.builder().build();
		milkBrandsContainer.setMilkBrandBean(savedMilkBrandBean);
		linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getAllAvailableMilkBrands())
			.withRel("link_getAllAvailableMilkBrands")
			.getHref(), "link_getAllAvailableMilkBrands");
		milkBrandsContainer.add(linkGateway);
		LOGGER.info("MilkBrandsController.saveMilkBrand() --- milkBrandsContainer: " + milkBrandsContainer);
		LOGGER.debug("MilkBrandsController.saveMilkBrand() --- END");
		return new ResponseEntity<>(milkBrandsContainer, HttpStatus.OK);
	}

	@GetMapping("/{milkBrandId}")
	public ResponseEntity<MilkBrandsContainer> getMilkBrandById(@PathVariable("milkBrandId") int milkBrandId) {
		LOGGER.debug("MilkBrandsController.getMilkBrandById() --- START");
		LOGGER.info("MilkBrandsController.getMilkBrandById() --- milkBrandId: " + milkBrandId);
		MilkBrandBean milkBrandBean = milkBrandService.getMilkBrandById(milkBrandId);
		Link linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getMilkBrandById(milkBrandId))
			.withRel("link_getMilkBrandById")
			.getHref(), "link_getMilkBrandById");
		milkBrandBean.add(linkGateway);
		LOGGER.info("MilkBrandsController.getMilkBrandById() --- milkBrandBean: " + milkBrandBean);
		MilkBrandsContainer milkBrandsContainer = MilkBrandsContainer.builder()
				.milkBrandBean(milkBrandBean)
				.build();
		linkGateway = util.getLinkGateway(WebMvcLinkBuilder.linkTo(methodOn(MilkBrandsController.class)
				.getAllAvailableMilkBrands())
			.withRel("link_getAllAvailableMilkBrands")
			.getHref(), "link_getAllAvailableMilkBrands");
		milkBrandsContainer.add(linkGateway);
		LOGGER.info("MilkBrandsController.getMilkBrandById() --- milkBrandsContainer: " + milkBrandsContainer);
		LOGGER.debug("MilkBrandsController.getMilkBrandById() --- END");
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

	@ExceptionHandler(value = {DairyProductNotAvailableException.class})
	public ResponseEntity<Void> handleDairyProductNotAvailableException(DairyProductNotAvailableException dairyProductNotAvailableException) {
		LOGGER.debug("MilkBrandsController.handleDairyProductNotAvailableException() --- START");
		LOGGER.info("MilkBrandsController.handleDairyProductNotAvailableException() --- " +
				dairyProductNotAvailableException.getMessage() +
				", returning No Content 204");
		LOGGER.debug("MilkBrandsController.handleDairyProductNotAvailableException() --- END");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(value = {MilkBrandAlreadyExistsException.class})
	public ResponseEntity<String> handleMilkBrandAlreadyExistsException(MilkBrandAlreadyExistsException milkBrandAlreadyExistsException) {
		LOGGER.debug("MilkBrandsController.handleMilkBrandAlreadyExistsException() --- START");
		LOGGER.info("MilkBrandsController.handleMilkBrandAlreadyExistsException() --- " +
				milkBrandAlreadyExistsException.getMessage() +
				", returning Conflict 409");
		LOGGER.debug("MilkBrandsController.handleMilkBrandAlreadyExistsException() --- END");
		return new ResponseEntity<>(milkBrandAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = {MilkBrandNotAvailableException.class})
	public ResponseEntity<Void> handleMilkBrandNotAvailableException(MilkBrandNotAvailableException milkBrandNotAvailableException) {
		LOGGER.debug("MilkBrandsController.handleMilkBrandNotAvailableException() --- START");
		LOGGER.info("MilkBrandsController.handleMilkBrandNotAvailableException() --- " +
				milkBrandNotAvailableException.getMessage() +
				", returning No Content 204");
		LOGGER.debug("MilkBrandsController.handleMilkBrandNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}
}
