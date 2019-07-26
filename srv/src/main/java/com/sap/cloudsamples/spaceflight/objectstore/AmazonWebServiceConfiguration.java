package com.sap.cloudsamples.spaceflight.objectstore;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;




/**

 * This is AWS Credentials Configuration class

 *

 */


public class AmazonWebServiceConfiguration {

	

	private String accessKeyId = new String ("AKIA5PG4DDJPXZGDLWHM");

	private String bucket  = new String ("hcp-b41901d4-7bfc-4829-a6af-a2fee94cb8fb");

	private String secretAccessKey  = new String ("9TjQV/aGBbiSMKwIxrkC4xOQr9y9oq8mOiWw40gK");

	AmazonWebServiceConfiguration(){
		
	}

	public String getAccessKeyId() {

		return accessKeyId;

	}

	public void setAccessKeyId(final String accessKeyId) {

		this.accessKeyId = accessKeyId;

	}

	public String getBucket() {

		return bucket;

	}

	public void setBucket(final String bucket) {

		this.bucket = bucket;

	}

	public String getSecretAccessKey() {

		return secretAccessKey;

	}

	public void setSecretAccessKey(final String secretAccessKey) {

		this.secretAccessKey = secretAccessKey;

	}



	/**

	 * @return blobStoreContext

	 */

	public BlobStoreContext getBlobStoreContext() {

		return ContextBuilder.newBuilder("aws-s3")
				.credentials(this.getAccessKeyId(), this.getSecretAccessKey())
				.buildView(BlobStoreContext.class);

	}

	

}