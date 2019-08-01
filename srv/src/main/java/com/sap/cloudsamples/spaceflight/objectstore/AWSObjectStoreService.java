package com.sap.cloudsamples.spaceflight.objectstore;

import java.io.InputStream;
import java.util.List;

import org.jclouds.blobstore.BlobStoreContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AWSObjectStoreService implements ObjectStoreService {

    
	private BlobStoreContext context;
	private final ObjectStoreRepository repository;
	private final String containerName;
	private static Logger logger = LoggerFactory.getLogger(AWSObjectStoreService.class);


	public AWSObjectStoreService(ObjectStoreRepository repository) {
		AmazonWebServiceConfiguration awsConfig = new AmazonWebServiceConfiguration();
		this.context =  awsConfig.getBlobStoreContext();
		this.repository = repository;
		this.containerName = awsConfig.getBucket();
	}

	@Override
	public String uploadFile(byte[] bytes, String fileName, String contentType) {
		repository.setContext(this.context);
		logger.info("Upload started");
		String message = repository.uploadFile(containerName, bytes, fileName, contentType);
		logger.info("upload completed");
		return message;
	}

	public List<BlobFile> listObjects() {
		repository.setContext(this.context);
		List<BlobFile> files = repository.listFiles(containerName);
		return files;
	}

	@Override
	public InputStream getFile(String fileName) {
		repository.setContext(this.context);
		InputStream inputStream = repository.downloadFile(containerName, fileName);
		return inputStream;
	}

	@Override
	public boolean deleteFile(String fileName) {
		repository.setContext(this.context);
		boolean status = repository.deleteFile(containerName, fileName);
		return status;

	}

	@Override
	public boolean isBlobExist(String fileName) {
		repository.setContext(this.context);
		boolean status = repository.isBlobExist(containerName, fileName);
		return status;
	}
}