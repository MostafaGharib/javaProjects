package com.mostafa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mostafa.MainClass.CalculateResponseTime;
import com.mostafa.MainClass.ThreadCounter;

public class GetRequestTask implements Runnable {

	ThreadCounter counter ;
	int requestsPerThread ;
	int requestTimeoutlimit ;
	String urlArgument ;
	long startTime= 0L ;
	//static ArrayList<Long>  elapsedTimeList  = new ArrayList<>() ;
	static List<Long> elapsedTimeList  = new CopyOnWriteArrayList<Long>() ;
	static int successfulRequests ;
	static int faildRequests ;
	
	CalculateResponseTime  calculateResponseTime = new CalculateResponseTime() ;
	
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
		
	//	System.out.println("before addResponseTime(elapsedTime)  in run ");
		calculateResponseTime.addResponseTime(elapsedTimeList);
	//	System.out.println("After addResponseTime(elapsedTime) in run  ");
		
		
		calculateResponseTime.setSuccessfulRequests(successfulRequests);
		calculateResponseTime.setFaildRequests(faildRequests);
		
	//	System.out.println("number of successful requests : " + successfulRequests);
	//	System.out.println("number of faild requests : " + faildRequests);
	}
	
	
	private void sendGet( int requestTimeoutlimit , String urlArgument) throws ProtocolException {
		String query = urlArgument;
		URL url;
		try {
			url = new URL(query);
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) url.openConnection();	
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent","Mozilla/5.0");
				int responseCode = connection.getResponseCode();
				connection.setConnectTimeout(requestTimeoutlimit);
			//	System.out.println("response code : " + responseCode );
				if (responseCode == 200) {
					
					
					successfulRequests++ ;
					
				startTime = System.currentTimeMillis();
					
				String response = getResponse(connection);
				
				elapsedTimeList.add(System.currentTimeMillis() - startTime);
				
			//	System.out.println("response: " + response.toString());
				} else {
				System.out.println("Bad Response Code: " + responseCode);
				faildRequests++ ;
				}
				
				
			}catch(java.net.SocketException e){
				// e.printStackTrace();
				System.out.println("connection time out");
				faildRequests++ ;
				
			}
			
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("please check your internet connection or try again later");
			}
			
			
			
		} catch (MalformedURLException e) {
			
			System.out.println("url is invalid , please Enter a valid url");
		}
		
	
}
	private String getResponse(HttpURLConnection connection) {
		
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {
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
