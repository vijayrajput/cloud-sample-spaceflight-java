package com.sap.cloudsamples.spaceflight.adobe;


import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import java.net.URI;

public class AdobeService {
	

public AdobeService(){
		// resolve destination
        final Destination destination = DestinationAccessor.getDestination(AdobeDestination.DESTINATION_NAME);
       // final URI uri = destination.getUri();
        //final URI path = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, null);
       // String clientId = destination.get
}
}
