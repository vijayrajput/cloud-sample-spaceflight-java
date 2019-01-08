package com.sap.cloudsamples.spaceflight.s4;

import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.connectivity.ErpDestination;

public class S4Config {

	public static final ErpConfigContext DESTINATION = new ErpConfigContext(System.getenv("DESTINATION_NAME_S4") != null //
			? System.getenv("DESTINATION_NAME_S4") //
			: ErpDestination.getDefaultName() //
	);

}