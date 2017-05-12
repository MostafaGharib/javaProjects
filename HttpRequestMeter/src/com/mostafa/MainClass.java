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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
	
		Scanner scanner = new Scanner(System.in);
		ThreadCounter counter = new ThreadCounter();
		CalculateResponseTime calculateResponseTime = new CalculateResponseTime() ;
		int noOfThreads ;
		int requestsPerThread;
		int requestTimeoutlimit;
		Matcher m ;
		String urlArgument ;
		
		   System.out.println();
		   System.out.println();
		   System.out.println("\t \t Welcome to HttpRequestMeter");
		   System.out.println("*************************************************************");
		   System.out.println("please Enter the URL  for Example (http://www.google.com)");
		   urlArgument =scanner.next();
//		   
//		   do {
//			   
////			   while (!scanner.hasNext()) {
////				   String input = scanner.next();
////                   System.out.printf("%s is not a valid number.\n", input);
////			   }
//		     
//           
//          
//           
//          
//		   } while (!isURLValid(urlArgument , false));
           
           do {
        	   System.out.println("please Enter the number of threads (number) ");
               while (!scanner.hasNextInt()) {
                   String input = scanner.next();
                   System.out.printf("%s is not a valid number.\n", input);
               }
               noOfThreads= scanner.nextInt();
           } while (noOfThreads < 0);
           
           do {
        	   System.out.println("please Enter the number of requests per threads");
               while (!scanner.hasNextInt()) {
                   String input = scanner.next();
                   System.out.printf("%s is not a valid number.\n", input);
               }
               requestsPerThread= scanner.nextInt();
           } while (requestsPerThread < 0);
           
           do {
        	   System.out.println("please Enter the request timeout limit in (ms) ");
               while (!scanner.hasNextInt()) {
                   String input = scanner.next();
                   System.out.printf("%s is not a valid number.\n", input);
               }
               requestTimeoutlimit= scanner.nextInt();
           } while (requestTimeoutlimit < 0);
           
          
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
		  // System.out.println("all task finished successfully");
		   System.out.println("End Time : " + endTime);
		   double totalTimeConsumed =  (System.currentTimeMillis() - t1) / 1000.0 ;
		   System.out.println("Total Time Consumed : " + totalTimeConsumed + " seconds");
		   System.out.println("number of fired thread : " + counter.getCounter() + " Thread");
		   System.out.println("Max Response Time : " + calculateResponseTime.getmaxResonseTime() + " ms");
		   System.out.println("Min Response Time : " + calculateResponseTime.getminResonseTime() + " ms" );
		   System.out.println("Average ResponseTime : " + calculateResponseTime.getAverageTime() + " ms" );
		   System.out.println("number of final  successful requests : " + calculateResponseTime.successfulRequests);
		   System.out.println("number of final  faild requests : " + calculateResponseTime.faildRequests); 
		   System.out.println("Rate : " + calculateResponseTime.getRate(noOfThreads) + " Request/minute");
		}
	
	public static boolean isURLValid(String url, boolean forcehttps) {
	    String regex = "";
	    if (forcehttps) {
	        regex = "^(https):\\/\\";
	    } else {
	        regex = "^(https?):\\/\\";
	    }
	    regex += "((([a-z0-9]\\.|[a-z0-9][a-z0-9-]*[a-z0-9]\\.)*"
	            + "[a-z][a-z0-9-]*[a-z0-9]"
	            + "|((\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])\\.){3}"
	            + "(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])"
	            + ")(:\\d+)?)"
	            + "(#([a-z0-9$_\\.\\+!\\*\\'\\(\\),;:@&=-]|%[0-9a-f]{2})*)?(\\/)"
	            + "$/i";

	    Pattern p = Pattern.compile(regex);
	    Matcher m = p.matcher(url); // get a matcher object
	    return m.matches();
	}
	
	
	
	public static class ThreadCounter {
		int counter = 0  ;
		int getCounter() { return counter ; }
		public synchronized void updateCounter() {
			counter ++ ;
		}
		

	}
	
	public static class CalculateResponseTime {
		
		
		
	  static List<Long> responseList  = new CopyOnWriteArrayList<Long>() ;    // to avoid concurrentModification Exception
	  static int successfulRequests ;
	  static int faildRequests ;
	  double rate ; 
		
		
		public CalculateResponseTime() {
			// TODO Auto-generated constructor stub
		
		}
		
		
		
		public  void addResponseTime (List<Long>  elapsedTimeList) {
			
			synchronized(this) {
		    final  ListIterator<Long> iterator = elapsedTimeList.listIterator();
			while(iterator.hasNext()){
			responseList.add(iterator.next()) ;
			}
			}
	
		}
		
		Long getmaxResonseTime () {
			
			Long  value = 0l ;
			Collections.sort(responseList);
			ListIterator<Long> iterator = responseList.listIterator();
		    while(iterator.hasNext()){
		   	 iterator.next();
		    	if(!iterator.hasNext()){
		  //          System.out.println("at end of the list");
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
				//System.out.println("iterator.next()" + iterator.next());
		    	if(!iterator.hasPrevious()){
		        //   System.out.println("at start of the list");
		            value = iterator.next() ; 
		            break ;
		    	}
		    }
		       
		    return value ;
		}
		
		Long getAverageTime() {
			
		//	System.out.println("responseList.size() : " + responseList.size() );
			Long sum = 0L ;
			for (int i = 0 ; i < responseList.size()-1 ; i++ )
			sum +=  (Long)responseList.get(i)	;
			try {
			return (sum / responseList.size());
			}catch (ArithmeticException e) {
				return 0l ;
			}
		}
		
		public int getSuccessfulRequests() {
			return successfulRequests;
		}
		public void setSuccessfulRequests(int successfulRequests) {
			this.successfulRequests = successfulRequests;
		}
		public int getFaildRequests() {
			return faildRequests;
		}
		public void setFaildRequests(int faildRequests) {
			this.faildRequests = faildRequests;
		}
		
		
		public double getRate(int noOfThreads) {
			rate = (noOfThreads/getAverageTime().doubleValue())*60;
			return rate;
		}
	}

}


