package com.mostafa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpURLConnectionExample {
	
	public static void main(String[] args) throws Exception {
	
		Scanner scanner = new Scanner(System.in);
		ThreadCounter counter = new ThreadCounter();
		
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
		}
	
	public static class ThreadCounter {
		int counter = 0  ;
		int getCounter() { return counter ; }
		public synchronized void updateCounter() {
			counter ++ ;
		}

	}
	
	

}


