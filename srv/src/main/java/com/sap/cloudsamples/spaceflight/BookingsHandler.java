package com.sap.cloudsamples.spaceflight;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityDataBuilder;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.AfterCreate;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeCreate;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeUpdate;
import com.sap.cloud.sdk.service.prov.api.exception.DatasourceException;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeCreateResponse;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeUpdateResponse;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseBuilderWithBody;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseImpl;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseWithBody;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.request.UpdateRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponseAccessor;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponseBuilder;
import com.sap.cloudsamples.spaceflight.s4.CreateTemporaryAddressCommand;

/**
 * Request handler for <code>BookingService.Bookings</code> entity.
 */
public class BookingsHandler {

	private static final Logger logger = LoggerFactory.getLogger(BookingsHandler.class);

	static final String BOOKING_SERVICE = "BookingService";
	private static final String PROPERTY_ID = "ID";
	private static final String PROPERTY_NAME = "Name";

	private static final String ENTITY_BOOKINGS = "Bookings";

	private static final String ENTITY_ITINERARIES = BOOKING_SERVICE + ".Itineraries";
	private static final String PROPERTY_BOOKING_BOOKINGNO = "BookingNo";
	private static final String PROPERTY_BOOKING_ITINERARYID = "Itinerary_ID";
	private static final String PROPERTY_BOOKING_CUSTOMERID = "Customer_ID";

	/**
	 * Called before an entity instance is created
	 */
	@BeforeCreate(serviceName = BOOKING_SERVICE, entity = ENTITY_BOOKINGS)
	public BeforeCreateResponse beforeBookingCreate(CreateRequest req, ExtensionHelper helper)
			throws ODataException, DatasourceException {
		return beforeUpsert(req.getData(), helper.getHandler(), true);
	}

	/**
	 * Called before an entity instance is updated
	 */
	@BeforeUpdate(serviceName = BOOKING_SERVICE, entity = ENTITY_BOOKINGS)
	public BeforeUpdateResponse beforeBookingUpdate(UpdateRequest req, ExtensionHelper helper)
			throws ODataException, DatasourceException {
		return beforeUpsert(req.getData(), helper.getHandler(), false);
	}

	/**
	 * Handles BeforeCreate and BeforeUpdate of Bookings
	 */
	private PreExtensionResponseWithBody beforeUpsert(EntityData reqData, DataSourceHandler dataSource, boolean insert)
			throws ODataException, DatasourceException {
		ErrorResponseBuilder errorResponseBuilder = ErrorResponse.getBuilder();
		boolean success = true;
		EntityDataBuilder entityBuilder = EntityData.getBuilder(reqData);

		// get the booking's customer from remote, and store it in the local DB
		success &= fetchAndSaveCustomer(reqData, dataSource, errorResponseBuilder);

		if (!success) {
			return new PreExtensionResponseImpl(
					errorResponseBuilder.setStatusCode(HttpStatus.SC_BAD_REQUEST).response());
		}

		if (insert) {
			// compute and set a new booking number
			entityBuilder.addElement(PROPERTY_BOOKING_BOOKINGNO, computeBookingNumber());
		}

		return new PreExtensionResponseBuilderWithBody(entityBuilder.buildEntityData(ENTITY_BOOKINGS)).response();
	}

	/**
	 * Retrieves the booking's customer from remote, and stores it in the local DB
	 * 
	 * @return <code>false</code> in case of errors
	 */
	private static boolean fetchAndSaveCustomer(EntityData reqData, DataSourceHandler dataSource,
			ErrorResponseBuilder errorResponseBuilder) {
		if (reqData.contains(PROPERTY_BOOKING_CUSTOMERID)) {
			String custId = String.valueOf(reqData.getElementValue(PROPERTY_BOOKING_CUSTOMERID));
			try {
				Customer customer = CustomersReplicator.fetchCustomer(custId, true);
				CustomersReplicator.saveCustomer(customer, dataSource);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				addErrorMessage(errorResponseBuilder, PROPERTY_BOOKING_CUSTOMERID, "NoSuchCustomer", custId);
				return false;
			}
		}
		return true;
	}

	private static void addErrorMessage(ErrorResponseBuilder responseBuilder, String target, String messageKey,
			Object... messageArgs) {
		responseBuilder.setMessage(messageKey, messageArgs).addErrorDetail(messageKey, target, messageArgs);
	}

	private static String computeBookingNumber() {
		String no = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()); // e.g. 20180810
		no += "/" + RandomStringUtils.randomAlphanumeric(5).toUpperCase(Locale.ENGLISH); // short random string
		return no;
	}

	@AfterCreate(serviceName = BOOKING_SERVICE, entity = ENTITY_BOOKINGS)
	public CreateResponse afterBookingCreate(final CreateRequest request, final CreateResponseAccessor response,
			final ExtensionHelper helper) {
		logger.info("afterBookingCreate with request data {} and response data {}", request.getMapData(),
				response.getEntityData().asMap());

		final String customerId = response.getEntityData().getElementValue(PROPERTY_BOOKING_CUSTOMERID).toString();
		final String itineraryId = response.getEntityData().getElementValue(PROPERTY_BOOKING_ITINERARYID).toString();
		final Timestamp dateOfTravel = (Timestamp) response.getEntityData().getElementValue("DateOfTravel");

		new CreateTemporaryAddressCommand(new DefaultBusinessPartnerService(), customerId,
				dateOfTravel.toLocalDateTime(), retrieveDestinationName(itineraryId, helper)).queue();

		return response.getOriginalResponse();
	}

	private String retrieveDestinationName(final String itineraryId, final ExtensionHelper helper) {
		try {
			final EntityData itinerary = helper.getHandler().executeRead(ENTITY_ITINERARIES,
					ImmutableMap.of(PROPERTY_ID, itineraryId), Collections.singletonList(PROPERTY_NAME));
			final String itineraryName = String.valueOf(itinerary.getElementValue(PROPERTY_NAME));

			final Pattern pattern = Pattern.compile("\\sto\\s(.+?)\\s", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(itineraryName);
			if (matcher.find() && matcher.groupCount() > 0) {
				return matcher.group(1);
			}
		} catch (final DatasourceException e) {
			logger.error("Error while reading itinerary {}", itineraryId, e);
		} catch (final RuntimeException e) {
			logger.error("Error happened: ", e);
		}

		return "Mars";
	}
}