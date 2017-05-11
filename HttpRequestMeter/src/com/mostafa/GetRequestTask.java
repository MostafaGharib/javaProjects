package com.mostafa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.mostafa.HttpURLConnectionExample.ThreadCounter;

public class GetRequestTask implements Runnable {

	ThreadCounter counter ;
	int requestsPerThread ;
	int requestTimeoutlimit ;
	String urlArgument ;
	
	
	public GetRequestTask(int requestsPerThread, int requestTimeoutlimit , String urlArgument ,ThreadCounter counter) {
		super();
		this.requestsPerThread = requestsPerThread;
		this.requestTimeoutlimit = requestTimeoutlimit;
		this.urlArgument =  urlArgument ;
		this.counter = counter ;
	}


	@Override
	public void run() {
		
		counter.updateCounter();
		
	//	GetRequestTask http = new GetRequestTask();
		try {
			
			for (int i = 0 ; i< requestsPerThread ; i++ )
			this.sendGet(requestTimeoutlimit ,urlArgument);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void sendGet( int requestTimeoutlimit , String urlArgument) throws ProtocolException {
		String query = urlArgument;
		URL url;
		try {
			url = new URL(query);
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) url.openConnection();

//				System.out.println( "connection.getInputStream()  :     "+ connection.getInputStream());
//				System.out.println( "connection.getConnectTimeout()  :     "+ connection.getConnectTimeout());
//				System.out.println( "connection.getContentLengthLong()  :     "+ connection.getContentLengthLong());
//				System.out.println( "connection.getDate()  :     "+ connection.getDate());
//				System.out.println( "connection.getResponseCode()  :     "+ connection.getResponseCode());
//				System.out.println( "connection.getResponseMessage()  :     "+ connection.getResponseMessage());
//				System.out.println( "connection.getContentEncoding()  :     "+ connection.getContentEncoding());
//				System.out.println( "connection.getContentType()  :     "+ connection.getContentType());
//				System.out.println( "connection.getExpiration())  :     "+ connection.getExpiration());
//				System.out.println( "connection.getRequestMethod()  :     "+ connection.getRequestMethod());
//				System.out.println( "************************************************************************");
				
				
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent","Mozilla/5.0");
				int responseCode = connection.getResponseCode();
				connection.setConnectTimeout(requestTimeoutlimit);
			//	getResponseHeader(connection) ;
				if (responseCode == 200) {
				String response = getResponse(connection);
			//	System.out.println("response: " + response.toString());
				} else {
				System.out.println("Bad Response Code: " + responseCode);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("please check your internet connection or try again later");
			}
			
			
			
		} catch (MalformedURLException e) {
			
			System.out.println("url is invalid , please Enter a valid url");
		}
		
	
}
	private String getResponse(HttpURLConnection connection) {
		try (BufferedReader br = new BufferedReader(
		new InputStreamReader(
		connection.getInputStream()));) {
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = br.readLine()) != null) {
		response.append(inputLine);
		}
		br.close();
		return response.toString();
		} catch (IOException ex) {
		// Handle exceptions
		}
		return "";
		}
	private void getResponseHeader(HttpURLConnection connection) {
		Map<String, List<String>> map = connection.getHeaderFields();

		System.out.println("Printing Response Header...\n");

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey()
	                           + " ,Value : " + entry.getValue());
		}
	}
	
}
