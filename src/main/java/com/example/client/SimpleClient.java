package com.example.client;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.model.Skier;

public class SimpleClient {
	
	    private static HttpResponse<String> response;
	    private static  HttpClient client;
	    private static final ConcurrentLinkedQueue<Skier> EVENT_QUEUE = new ConcurrentLinkedQueue<>();
	    public static long startTime;
	    public static long endTime;
	    private static final String BASE_URL = "http://localhost:8080";

	    public static void main(String[] args) {
	    	
	    	DataGenerator();
	    	
	    	client = HttpClient.newHttpClient();
	    	startTime = System.currentTimeMillis();
	    	
	    	for( int i = 1; i<=500; i++) {
	    		 Skier skier = new Skier(EVENT_QUEUE.poll());
	    	
	            try {
	            	
	            	 URI uri = URI.create(String.format(BASE_URL + "/skiers/%d/seasons/%s/days/%d/skiers/%d",
                     		skier.getResortID(), skier.getSeasonYear(), skier.getDayID(), skier.getSkierID()));
                     System.out.println(uri);
                 	HttpRequest request = HttpRequest.newBuilder()
                             .uri(uri)
                             .header("Content-Type", "application/json")
                             .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"time\": %d, \"liftID\": %d}", skier.getTime(), skier.getLiftID())))
                             .build();

					response = client.send(request, HttpResponse.BodyHandlers.ofString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.printf("Response code for skier %d: %d%n", 3456, response.statusCode());
	    	}
	    	endTime = System.currentTimeMillis();
	    	System.out.println("Wall time : " + endTime + " - " + startTime + " = " + (endTime - startTime)+ " ms"); 
	    }
	    
	    public static void DataGenerator() {
	    	
	    	 for (int i = 0; i < 500; i++) {
	    		 
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
