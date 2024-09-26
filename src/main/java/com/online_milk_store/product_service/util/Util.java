package com.online_milk_store.product_service.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class Util {
	final static private Logger LOGGER = LogManager.getLogger(Util.class);

	@Value("${gateway.base-url}")
	private String gatewayBaseUrl;

	@Value("${system.name.oms}")
	private String systemName_OMS;

	public Link getLinkGateway(String url, String relation) {
		LOGGER.debug("Util.getLinkGateway() --- START");
		LOGGER.info("Util.getLinkGateway() --- productServiceUrl: " + url);
		Link linkGateway = null;
		try {
			URI originalUri = new URI(url);
			URI gatewayUri = new URI(gatewayBaseUrl);
			String updatedForGateway = new URI(originalUri.getScheme(),
						originalUri.getUserInfo(),
						gatewayUri.getHost(),
						gatewayUri.getPort(),
						"/" + systemName_OMS + originalUri.getPath(),
						originalUri.getQuery(),
						originalUri.getFragment()).toString();
				linkGateway = Link.of(updatedForGateway, relation);
			} catch (URISyntaxException e) {
				LOGGER.error("Util.getLinkGateway() --- URISyntaxException: " + e.getMessage());
			}
		LOGGER.info("Util.getLinkGateway() --- linkGateway: " + linkGateway);
		LOGGER.debug("Util.getLinkGateway() --- END");
		return linkGateway;
	}
}
