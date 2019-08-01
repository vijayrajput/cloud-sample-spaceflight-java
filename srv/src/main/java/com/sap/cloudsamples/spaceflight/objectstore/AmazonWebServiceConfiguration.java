package com.sap.cloudsamples.spaceflight.objectstore;

import com.sap.cloud.sdk.cloudplatform.ScpCfCloudPlatform;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This is AWS Credentials Configuration class
 *
 * 
 * 
 */

public class AmazonWebServiceConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(AmazonWebServiceConfiguration.class);
	private String accessKeyId;

	private String bucket;

	private String secretAccessKey;

	public AmazonWebServiceConfiguration() {
		logger.info("AWS Credential Start bucket");
		ScpCfCloudPlatform scpService = new ScpCfCloudPlatform();
		JsonObject jsonObj = scpService.getServiceCredentials("objectstore");
		logger.info("AWS Credential:  " + jsonObj.toString());
		if (jsonObj.has("bucket")) {
			this.bucket = jsonObj.get("bucket").getAsString();
		}
		if (jsonObj.has("access_key_id")) {
			this.accessKeyId = jsonObj.get("access_key_id").getAsString();
		}
		if (jsonObj.has("secret_access_key")) {
			this.secretAccessKey = jsonObj.get("secret_access_key").getAsString();

		}
		logger.info("AWS Credential: bucket: " + this.bucket);
		logger.info("AWS Credential: accessKeyId: " + this.accessKeyId);
		logger.info("AWS Credential: secretAccessKey: " + this.secretAccessKey);
	}

	public String getAccessKeyId() {

		return this.accessKeyId;

	}

	public void setAccessKeyId(final String accessKeyId) {

		this.accessKeyId = accessKeyId;

	}

	public String getBucket() {

		return this.bucket;

	}

	public void setBucket(final String bucket) {

		this.bucket = bucket;

	}

	public String getSecretAccessKey() {

		return this.secretAccessKey;

	}

	public void setSecretAccessKey(final String secretAccessKey) {

		this.secretAccessKey = secretAccessKey;

	}

	/**
	 * 
	 * @return blobStoreContext
	 * 
	 */

	public BlobStoreContext getBlobStoreContext() {

		return ContextBuilder.newBuilder("aws-s3").credentials(this.getAccessKeyId(), this.getSecretAccessKey())
				.buildView(BlobStoreContext.class);

	}

}