package com.example.client;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.model.Skier;

public class ClientMultithreaded{
    private static final String BASE_URL = "http://localhost:8080"; // "http://20.220.214.172:8080";
    private static final int NUM_THREADS = 32;
    private static final int NUM_REQUESTS_PER_THREAD = 1000;
    private static final int MAX_REQUESTS = 10000;
    private static volatile int numRequests = 0;
    private static final int NUM_EVENTS = 10000;
    private static final ConcurrentLinkedQueue<Skier> EVENT_QUEUE = new ConcurrentLinkedQueue<>();
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 1000;
    private static HttpResponse<String> response;
    public static long startTime;
    public static long endTime;
    public static int SUCC_THREADS = 0;
    public static int UNSUCC_THREADS = 0;
 // Create list to store timestamps
    static List<String> timestampPairs = new ArrayList<>();
    private static final String CSV_FILE = "response_times.csv";

    

    public static void main(String[] args) {
        
    	DataGenerator();
              
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_THREADS; i++) {
        	
            executor.execute(new Requests(i));
        }
        executor.shutdown();
        
        while(!executor.isTerminated()) {}
        endTime = System.currentTimeMillis();
        
     
        // Write timestamp data to CSV file
        FileWriter writer;
		try {
			writer = new FileWriter(CSV_FILE);
			 for (String timestampPair : timestampPairs) {
		            writer.append(timestampPair);
		            writer.append('\n');
		        }
		        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Calculate mean and median
		
        List<Long> differences = new ArrayList<>();
        for (String timestampPair : timestampPairs) {
            String[] timestamps = timestampPair.split(",");
            long beforeTime = Long.parseLong(timestamps[0]);
            long afterTime = Long.parseLong(timestamps[1]);
            differences.add(afterTime - beforeTime);
        }

     // Calculate mean and median response times
        long sum = 0;
        for (Long difference : differences) {
            sum += difference;
        }
        double mean = (double) sum / differences.size();

        differences.sort(null);
        double median;
        if (differences.size() % 2 == 0) {
            int mid = differences.size() / 2;
            median = (differences.get(mid - 1) + differences.get(mid)) / 2.0;
        } else {
            median = differences.get(differences.size() / 2);
        }

        // Calculate throughput
        //double differenceTime = (double) differences.get(differences.size() - 1) - differences.get(0);
       // double throughput = (double) differences.size() / (differenceTime / 1000);

        // Calculate p99 response time
        int p99Index = (int) Math.ceil(differences.size() * 0.99) - 1;
        long p99ResponseTime = differences.get(p99Index);
        
        long minResponseTime = Collections.min(differences);
        long maxResponseTime = Collections.max(differences);
        
        System.out.println("Successful threads : "+ SUCC_THREADS);
        System.out.println("Unsuccessful Threads : "+ UNSUCC_THREADS);
        System.out.println("Wall time : " + endTime + " - " + startTime + " = " + (endTime - startTime)+ " ms"); 
        System.out.println("Mean response time (ms): " + mean);
        System.out.println("Median response time (ms): " + median);
        System.out.println("Throughput  (requests/second) : " + 1000*((double) NUM_EVENTS / ((double) (endTime - startTime))));
        System.out.println("p99 response time (ms): " + p99ResponseTime);
        System.out.println("Min response time (ms): " + minResponseTime);
        System.out.println("Max response time (ms): " + maxResponseTime);
        
   
    }

    static class Requests implements Runnable {
        private final int threadId;
        private final HttpClient client;

        Requests(int threadId) {
            this.threadId = threadId;
            this.client = HttpClient.newHttpClient();
        }

        public void run() {
            for (int i = 0; i < NUM_REQUESTS_PER_THREAD; i++) {
                synchronized (ClientMultithreaded.class) {
                    if (numRequests >= MAX_REQUESTS) {
                        break;
                    }
                    numRequests++;
                    Skier skier = new Skier(EVENT_QUEUE.poll());

                try {
                	
                	int retries = 0;
                    while (retries < MAX_RETRIES) {
                    	
                        long beforeTime = System.currentTimeMillis();
                        URI uri = URI.create(String.format(BASE_URL + "/skiers/%d/seasons/%s/days/%d/skiers/%d",
                        		skier.getResortID(), skier.getSeasonYear(), skier.getDayID(), skier.getSkierID()));
                        System.out.println(uri);
                    	HttpRequest request = HttpRequest.newBuilder()
                                .uri(uri)
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"time\": %d, \"liftID\": %d}", skier.getTime(), skier.getLiftID())))
                                .build();

                            response = client.send(request, HttpResponse.BodyHandlers.ofString());
                            long afterTime = System.currentTimeMillis();
                            timestampPairs.add(beforeTime + "," + afterTime);
                            System.out.printf("Thread %d - Request %d - Response code for skier %d: %d%n", threadId, i, skier.getSkierID(), response.statusCode());
                            
                        if (response.statusCode() >= 400 && response.statusCode() < 500 || response.statusCode() >= 500 && response.statusCode() < 600) {
                            retries++;
                            System.out.println("Request failed with response code " + response.statusCode() + ", retrying in " + RETRY_DELAY_MS + "ms...");
                            try {
                                Thread.sleep(RETRY_DELAY_MS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Request successful with response code " + response.statusCode());
                            SUCC_THREADS++;
                            break;
                        }
                    }
                    if (retries == MAX_RETRIES) {
                        System.out.println("Request failed after " + MAX_RETRIES + " retries.");
                        UNSUCC_THREADS++;
                        
                    }
                	                        
                } catch (IOException | InterruptedException e) {
                    System.err.printf("Error sending request for skier %d: %s%n", skier.getSkierID(), e.getMessage());
                    e.printStackTrace();
                    
                }
                
                }
            }
        }
    }
        
    public static void DataGenerator() {
    	
    	 for (int i = 0; i < NUM_EVENTS; i++) {
    		 
             int skierID = (int) (Math.random() * 99999) + 1;
             int resortID = (int) (Math.random() * 9) + 1;
             int liftID = (int) (Math.random() * 39) + 1;
             int seasonID = 2022;
             int dayID = 1;
             int time = (int) (Math.random() * 359) + 1;
        
             Skier skier = new Skier(skierID, time, liftID, resortID, String.valueOf(seasonID), dayID);
             EVENT_QUEUE.add(skier);
    	 }    	
    }
}
