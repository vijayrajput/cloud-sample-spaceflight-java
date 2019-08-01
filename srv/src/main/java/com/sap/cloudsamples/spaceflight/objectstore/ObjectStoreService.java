package com.sap.cloudsamples.spaceflight.objectstore;


import java.io.InputStream;
import java.util.List;



public interface ObjectStoreService {
	
	/**
	 * @param bytes
	 * @param name
	 * @param contentType
	 * @return
	 */
	public String uploadFile(byte[] bytes, String name, String contentType); 
	
	/**
	 * @param blobFile
	 * @return true/false if file has been deleted
	 */
	public boolean deleteFile(String fileName);
	
	/**
	 * @param file
	 * @return InputStream
	 */
	public InputStream getFile(String fileName);
	
	/**
	 * @return list of blobFile
	 */
	public List<BlobFile> listObjects();
	
	/**
	 * @param name
	 * @return
	 */
	public boolean isBlobExist(String name);
}
