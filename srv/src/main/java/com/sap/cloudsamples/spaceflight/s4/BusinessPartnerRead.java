package com.sap.cloudsamples.spaceflight.s4;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerByKeyFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

/**
 * Helper to read a {@link BusinessPartner}
 */
public class BusinessPartnerRead extends ErpCommand<BusinessPartner> {
	private String businessPartner;

	public BusinessPartnerRead(String businessPartner) {
		super(BusinessPartnerRead.class);
		this.businessPartner = businessPartner;
	}

	@Override
	protected BusinessPartner run() {

		BusinessPartnerByKeyFluentHelper service = new DefaultBusinessPartnerService()
				.getBusinessPartnerByKey(businessPartner);

		try {
			return service.execute(S4Config.DESTINATION);
		} catch (final ODataException e) {
			throw new HystrixBadRequestException(e.getMessage(), e);
		}
	}
}