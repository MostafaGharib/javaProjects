package com.mostafa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpURLConnectionExample {
	
	public static void main(String[] args) throws Exception {
	
		Scanner scanner = new Scanner(System.in);
		ThreadCounter counter = new ThreadCounter();
		CalculateResponseTime calculateResponseTime = new CalculateResponseTime() ;
		
		   System.out.println();
		   System.out.println();
		   System.out.println("\t \t Welcome to HttpRequestMeter");
		   System.out.println("*************************************************************");
		   System.out.println("please Enter the URL  for Example (http://www.google.com)");
           String urlArgument =scanner.next();
           
           System.out.println("please Enter the number of threads");
           int noOfThreads = scanner.nextInt();
           System.out.println("please Enter the number of requests per threads");
           int requestsPerThread = scanner.nextInt() ;
           System.out.println("please Enter the request timeout limit in (ms) ");
           int requestTimeoutlimit = scanner.nextInt();
           scanner.close();
           
           ExecutorService executor = Executors.newCachedThreadPool();
           
          
           
           String startTime =  new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(System.currentTimeMillis()));
           long t1 = System.currentTimeMillis() ;
           System.out.println("Start Time : " +  startTime);
           for (int i = 0; i < noOfThreads; i++) {
           
           executor.execute(new GetRequestTask(requestsPerThread , requestTimeoutlimit , urlArgument , counter ));
           
           }
           
		   executor.shutdown();
		   
		   while (!executor.isTerminated()) {}
		   
		   String endTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(System.currentTimeMillis()));
		   System.out.println("all task finished successfully");
		   System.out.println("End Time : " + endTime);
		   double totalTimeConsumed =  (System.currentTimeMillis() - t1) / 1000.0 ;
		   System.out.println("Total Time Consumed : " + totalTimeConsumed + " seconds");
		   System.out.println("number of fired thread : " + counter.getCounter());
		   System.out.println("Max Response Time : " + calculateResponseTime.getmaxResonseTime());
		   System.out.println("Min Response Time : " + calculateResponseTime.getminResonseTime() );
		   System.out.println("Average ResponseTime : " + calculateResponseTime.getAverageTime());
		    
		  
		}
	
	
	
	public static class ThreadCounter {
		int counter = 0  ;
		int getCounter() { return counter ; }
		public synchronized void updateCounter() {
			counter ++ ;
		}
		

	}
	
	public static class CalculateResponseTime {
		
	   static ArrayList<Long>  responseList  = new ArrayList<>() ;
		
		
		public CalculateResponseTime() {
			// TODO Auto-generated constructor stub
		
		}
		
		
		
		public synchronized void addResponseTime (Long r) {
			responseList.add(r) ;
			ListIterator<Long> iterator = responseList.listIterator();
		    while(iterator.hasNext()){
		    	System.out.println(iterator.next());
		    	
		    }
		    
		    try {
		     Thread.sleep(10000);
		    	 }
		    	 catch (InterruptedException ex) {
		    	 }
		}
		
		Long getmaxResonseTime () {
			
			Long  value = 0l ;
			Collections.sort(responseList);
			ListIterator<Long> iterator = responseList.listIterator();
		    while(iterator.hasNext()){
		    	System.out.println("iterator.hasNext()" + iterator.next());
		    	if(!iterator.hasNext()){
		            System.out.println("at end of the list");
		           value = iterator.previous() ; 
		            break ;
		    	}
		    }
		       
		    return value ;
		    
		}
		
		Long getminResonseTime () {
			
			Long  value = 0l ;
			Collections.sort(responseList);
			ListIterator<Long> iterator = responseList.listIterator();
			while(iterator.hasNext()){
		    	System.out.println("iterator.hasNext()" + iterator.next());
		    	if(!iterator.hasPrevious()){
		            System.out.println("at start of the list");
		            value = iterator.next() ; 
		            break ;
		    	}
		    }
		       
		    return value ;
		}
		
		Long getAverageTime() {
			
			System.out.println("responseList.size() : " + responseList.size() );
			Long sum = 0L ;
			for (int i = 0 ; i < responseList.size()-1 ; i++ )
			sum +=  (Long)responseList.get(i)	;
			
			return (sum / responseList.size());
				
		}
			
	}

}


