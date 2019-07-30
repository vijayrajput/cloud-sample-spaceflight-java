package com.sap.cloudsamples.spaceflight.objectstore;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.cloudsamples.spaceflight.adobe.AdobeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/testds")
public class TestObjectStore extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(TestObjectStore.class);
	private static final String FILE_NAME= "test.pdf"; 
	private static final String FILE_TYPE= "application/pdf"; 
	
	 public TestObjectStore() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
		uploadDocument(FILE_NAME);
		}
		catch (Exception Ex) {
			System.out.println("Dcument Upload Exception: "+Ex.getMessage());
			logger.error("Dcument Upload Exception: "+ Ex.getStackTrace().toString());
		}
		response.getWriter().append("Upload Successfully");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//parseService.getBodyfromRequest(request);
		String fileName = request.getParameter("order");
		
	//	String fileName = new String(FILE_NAME);
		ObjectStoreRepository repository =  new ObjectStoreRepository();
		logger.info("Before AWS Connection" );
		ObjectStoreService objService = new AWSObjectStoreService(repository);
		logger.info("After AWS Connection" );
		if (objService.isBlobExist(fileName)) {
				ServletOutputStream out = response.getOutputStream();
				InputStream inputStream = objService.getFile(fileName);
				byte[] bytes = new byte[1024];
				int bytesRead;
				response.setContentType(FILE_TYPE);
				
				while ((bytesRead = inputStream.read(bytes)) != -1) {
    			out.write(bytes, 0, bytesRead);
				}
				inputStream.close();
				out.close();
			} else {
				response.getWriter().append("File Not Found");
			}
	}
	
	private void uploadDocument(String fileName) {
		AdobeService adbService = new AdobeService();
		logger.info("Get Adobe Content");
		byte[] bytes = null;
		try {
			bytes = adbService.getFileContent(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectStoreRepository repository =  new ObjectStoreRepository();
		logger.info("Before AWS Connection" );
		ObjectStoreService objService = new AWSObjectStoreService(repository);
		logger.info("After AWS Connection" );
		String result = objService.uploadFile(bytes, fileName, FILE_TYPE);
		logger.info("After AWS Upload"+result);
	}
	

}
