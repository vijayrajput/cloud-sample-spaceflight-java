package com.sap.cloudsamples.spaceflight.adobe;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationDeclarator;

public class AdobeDestination extends DestinationDeclarator {
    public final static String DESTINATION_NAME = "AdobeService";

    public AdobeDestination() {
        super(DESTINATION_NAME);
    }
}