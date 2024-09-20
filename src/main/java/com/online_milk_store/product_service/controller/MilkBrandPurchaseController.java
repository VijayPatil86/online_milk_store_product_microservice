package com.online_milk_store.product_service.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_milk_store.product_service.bean.MilkBrandPurchaseBean;
import com.online_milk_store.product_service.exception.MilkBrandNotAvailableException;
import com.online_milk_store.product_service.service.MilkBrandPurchaseService;

@RestController
@RequestMapping("/milk-brand-item-purchase")
public class MilkBrandPurchaseController {
	static final private Logger LOGGER = LogManager.getLogger(MilkBrandPurchaseController.class);

	@Autowired
	private MilkBrandPurchaseService milkBrandPurchaseService;

	@PostMapping("/{milkBrandId}")
	public ResponseEntity<Void> addPurchaseRecord(@PathVariable(name = "milkBrandId") int milkBrandId,
			@RequestBody MilkBrandPurchaseBean milkBrandPurchaseBean) {
		LOGGER.debug("MilkBrandPurchaseController.addPurchaseRecord() --- START");
		LOGGER.info("MilkBrandPurchaseController.addPurchaseRecord() --- milkBrandId: " + milkBrandId);
		LOGGER.info("MilkBrandPurchaseController.addPurchaseRecord() --- milkBrandPurchaseBean: " + milkBrandPurchaseBean);
		milkBrandPurchaseService.addPurchaseRecord(milkBrandId, milkBrandPurchaseBean);
		LOGGER.debug("MilkBrandPurchaseController.addPurchaseRecord() --- END");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/** Exception Handling section **/
	@ExceptionHandler(value = {MilkBrandNotAvailableException.class})
	public ResponseEntity<Void> handleMilkBrandNotAvailableException(MilkBrandNotAvailableException milkBrandNotAvailableException) {
		LOGGER.debug("MilkBrandPurchaseController.handleMilkBrandNotAvailableException() --- START");
		LOGGER.info("MilkBrandPurchaseController.handleMilkBrandNotAvailableException() --- " +
				milkBrandNotAvailableException.getMessage() +
				", returning No Content 204");
		LOGGER.debug("MilkBrandPurchaseController.handleMilkBrandNotAvailableException() --- END");
		return ResponseEntity.noContent().build();
	}
}
