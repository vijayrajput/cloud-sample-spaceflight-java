package com.sap.cloudsamples.spaceflight.s4;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

/**
 * Creates a business partner address
 */
public class CreateTemporaryAddressCommand extends ErpCommand<BusinessPartnerAddress> {

	private static final Logger logger = CloudLoggerFactory.getLogger(CreateTemporaryAddressCommand.class);

	private static final String POSTAL_CODE_SPACE_TRAVEL = "99999";
	private static final String COUNTRY_SPACE_TRAVEL = "DE";
	private static final String STREET_SPACE_TRAVEL = "Starport";

	private final BusinessPartnerService service;

	private final String customerId;
	private final ZonedDateTime startDate;
	private final String destination;

	public CreateTemporaryAddressCommand(final BusinessPartnerService service,
			final String customerId, final LocalDateTime startDate, final String destination) {
		super(CreateTemporaryAddressCommand.class);

		this.service = service;
		this.customerId = customerId;
		this.startDate = startDate.atZone(ZoneId.systemDefault());
		this.destination = destination;
	}

	@Override
	protected BusinessPartnerAddress run() throws Exception {
		BusinessPartnerAddress toCreate = BusinessPartnerAddress.builder()
			.businessPartner(customerId)
			.cityName(destination)
			.validityStartDate(startDate)
			.validityEndDate(startDate.plusWeeks(42))
			.build();
		setCommonProperties(toCreate);

		logger.info("Creating temporary address {}", toCreate);
		try {
			return service.createBusinessPartnerAddress(toCreate).execute(S4Config.DESTINATION);
		} catch(ODataException e) {
			logger.error("Error while creating address", e);
			throw e;
		}
	}

	private void setCommonProperties(final BusinessPartnerAddress address) {
		address.setCountry(COUNTRY_SPACE_TRAVEL);
		address.setPostalCode(POSTAL_CODE_SPACE_TRAVEL);
		address.setStreetName(STREET_SPACE_TRAVEL);
	}

}