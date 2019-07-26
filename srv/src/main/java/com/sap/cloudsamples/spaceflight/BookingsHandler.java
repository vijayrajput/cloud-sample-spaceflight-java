package com.sap.cloudsamples.spaceflight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityDataBuilder;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.AfterRead;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeCreate;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeUpdate;
import com.sap.cloud.sdk.service.prov.api.exception.DatasourceException;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeCreateResponse;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeUpdateResponse;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseBuilderWithBody;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseImpl;
import com.sap.cloud.sdk.service.prov.api.exits.PreExtensionResponseWithBody;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.request.UpdateRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponseBuilder;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponseAccessor;
import com.sap.cloudsamples.spaceflight.adobe.AdobeService;
import com.sap.cloudsamples.spaceflight.objectstore.AWSObjectStoreService;
import com.sap.cloudsamples.spaceflight.objectstore.ObjectStoreRepository;
import com.sap.cloudsamples.spaceflight.objectstore.ObjectStoreService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for <code>BookingService.Bookings</code> entity.
 */
public class BookingsHandler {

	private static final Logger logger = LoggerFactory.getLogger(BookingsHandler.class);

	static final String BOOKING_SERVICE = "BookingService";
	private static final String PROPERTY_ID = "ID";

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
	
		@AfterRead(entity = "Bookings", serviceName = "BookingService")
	public ReadResponse afterReadBookings(ReadRequest req, ReadResponseAccessor res, ExtensionHelper helper) {
		//EntityData data = res.getEntityData();
		//TODO: add your custom logic / validations here...
		String no = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()); // e.g. 20180810
		no += "/" + RandomStringUtils.randomAlphanumeric(5).toUpperCase(Locale.ENGLISH); // short random string
		try{
		uploadDocument(no);
		}
		catch (Exception Ex) {
			System.out.println("Dcument Upload Exception: "+Ex.getMessage());
			logger.error("Dcument Upload Exception: "+ Ex.getStackTrace().toString());
		}
		return res.getOriginalResponse(); //use this API if no change is required and the original response can be returned as output.
		//return ReadResponse.setSuccess().setData(data).response(); //use this API if the payload is modified.
		//return ReadResponse.setError(ErrorResponse.getBuilder().setMessage("Read Operation Failed").response()); //use this API if should return error response.
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

	private String computeBookingNumber() {
		String no = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()); // e.g. 20180810
		no += "/" + RandomStringUtils.randomAlphanumeric(5).toUpperCase(Locale.ENGLISH); // short random string
		try{
		uploadDocument(no);
		}
		catch (Exception Ex) {
			System.out.println("Dcument Upload Exception: "+Ex.getMessage());
			logger.error("Dcument Upload Exception: "+ Ex.getStackTrace().toString());
		}
		return no;
	}
	
	private void uploadDocument(String fileName) {
		AdobeService adbService = new AdobeService();
		logger.error("Get Adobe Content");
		byte[] bytes = adbService.getFileContent(fileName);
		logger.error("After Adobe Content" + bytes.toString());
		ObjectStoreRepository repository =  new ObjectStoreRepository();
		logger.error("Before AWS Connection" );
		ObjectStoreService objService = new AWSObjectStoreService(repository);
		logger.error("After AWS Connection" );
		String result = objService.uploadFile(bytes, fileName, "application/pdf");
		logger.error("After AWS Upload"+result);
	}

}
