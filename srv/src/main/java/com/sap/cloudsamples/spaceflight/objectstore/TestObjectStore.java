package com.sap.cloudsamples.spaceflight.objectstore;

import java.io.IOException;

import javax.servlet.ServletException;
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
	
	 public TestObjectStore() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
		uploadDocument("test.pdf");
		}
		catch (Exception Ex) {
			System.out.println("Dcument Upload Exception: "+Ex.getMessage());
			logger.error("Dcument Upload Exception: "+ Ex.getStackTrace().toString());
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	private void uploadDocument(String fileName) {
		AdobeService adbService = new AdobeService();
<<<<<<< Upstream, based on origin/s4bp-final
		logger.error("Get Adobe Content");
		byte[] bytes = adbService.getFileContent(fileName);
		logger.error("After Adobe Content" + bytes.toString());
		ObjectStoreRepository repository =  new ObjectStoreRepository();
		logger.error("Before AWS Connection" );
		ObjectStoreService objService = new AWSObjectStoreService(repository);
		logger.error("After AWS Connection" );
		String result = objService.uploadFile(bytes, fileName, "application/pdf");
		logger.error("After AWS Upload"+result);
=======
		logger.info("Get Adobe Content");
		byte[] bytes = adbService.getFileContent(fileName);
		logger.info("After Adobe Content" + bytes.toString());
		ObjectStoreRepository repository =  new ObjectStoreRepository();
		logger.info("Before AWS Connection" );
		ObjectStoreService objService = new AWSObjectStoreService(repository);
		logger.info("After AWS Connection" );
		String result = objService.uploadFile(bytes, fileName, "application/pdf");
		logger.info("After AWS Upload"+result);
>>>>>>> 599e952 Object Store Upload
	}
	

}
