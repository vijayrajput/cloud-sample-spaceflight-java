package com.sap.cloudsamples.spaceflight.adobe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class AdobeService {

	private Map<String, String> destProperties;
	private static final Logger logger = LoggerFactory.getLogger(AdobeService.class);

	public AdobeService() {
		// resolve destination
		Destination destination = DestinationAccessor.getDestination(AdobeDestination.DESTINATION_NAME);
		this.destProperties = destination.getPropertiesByName();

	}

	public byte[] getFileContent(String id) throws IOException {

		String oAuthToken = getOAuthToken();

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet templateReq = new HttpGet(
				this.destProperties.get("URL") + "/ads.restapi/v1/forms/Test_Form/templates/Dummy_template");
		templateReq.addHeader("Authorization", "Bearer " + oAuthToken);
		HttpResponse respget = client.execute(templateReq);
		logger.error("PDF Template Status  Code " + respget.getStatusLine().getStatusCode());
		respget.getStatusLine().getStatusCode();
		HttpEntity entityGet = respget.getEntity();
		InputStream is2 = entityGet.getContent();
		String templateb64 = null;
		try {

			Reader reader = new InputStreamReader(is2);
			String text = CharStreams.toString(reader);
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(text);
			JsonObject jsonObject = je.getAsJsonObject();
			templateb64 = jsonObject.get("xdpTemplate").getAsString();

		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (is2 != null)
				is2.close();
		}

		logger.error("PDF Template Done ");
		PdfRequestBody body = new PdfRequestBody();
		body.setXmlData("");
		body.setXdpTemplate(templateb64);
		body.setTaggedPdf(0);
		body.setEmbedFont(0);
		Gson gson = new Gson();
		String base64 = null;

		try {

			URL adobeurl = new URL(this.destProperties.get("URL") + "/ads.restapi/v1/adsRender/pdf");
			HttpURLConnection urlConnection = (HttpURLConnection) adobeurl.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Authorization", "Bearer " + oAuthToken);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			OutputStream os = urlConnection.getOutputStream();
			os.write(gson.toJson(body).getBytes("UTF-8"));
			os.close();
			InputStream instream = null;
			if (urlConnection.getResponseCode() < 300) {
				logger.error("HTTP Post Call Successfull  : " + urlConnection.getResponseCode());
				instream = urlConnection.getInputStream();
				try {

					Reader reader = new InputStreamReader(instream);
					String text = CharStreams.toString(reader);
					JsonParser jp = new JsonParser();
					JsonElement je = jp.parse(text);
					JsonObject jsonObject = je.getAsJsonObject();
					base64 = jsonObject.get("fileContent").getAsString();

				} catch (IOException e) {
					logger.error("Pdf Renderer Parsing Error :" + e.getMessage());
				} finally {
					if (instream != null)
						instream.close();
				}
				
			} else {
				logger.error("HTTP Post Call Error" + urlConnection.getResponseCode());
				instream = urlConnection.getErrorStream();
				instream.close();
			}


		} catch (Exception e) {
			logger.error("HTTP Post Call Error" + e.getMessage());
		}

	
		byte[] bytes = Base64.getDecoder().decode(base64);
		return bytes;
	}

	private String getOAuthToken() throws IOException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(this.destProperties.get("tokenServiceURL"));

		/* AUTHENTICATION CREDENTIALS ENCODING */
		String base64Credentials = Base64.getEncoder().encodeToString(
				(this.destProperties.get("clientId") + ":" + this.destProperties.get("clientSecret")).getBytes());
		/* HEADER INFO */
		httpPost.addHeader("Authorization", "Basic " + base64Credentials);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		StringEntity input = null;
		try {
			input = new StringEntity("grant_type=client_credentials&scope=" + this.destProperties.get("scope"));
			httpPost.setEntity(input);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/* SEND AND RETRIEVE RESPONSE */
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* RESPONSE AS STRING */
		String result = null;
		InputStream in = response.getEntity().getContent();
		try {

			Reader reader = new InputStreamReader(in);
			String text = CharStreams.toString(reader);
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(text);
			JsonObject jsonObject = je.getAsJsonObject();
			result = jsonObject.get("access_token").getAsString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
		}

		return result;

	}
}
