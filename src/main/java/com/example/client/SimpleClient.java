package com.example.client;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimpleClient {
	
	    private static HttpResponse<String> response;
	    private static  HttpClient client;

	    public static void main(String[] args) {
	    	
	    	client = HttpClient.newHttpClient();
	    	
	            try {
	            	HttpRequest request = HttpRequest.newBuilder()
	                        .uri(URI.create(String.format("http://localhost:8080/skiers/%d/seasons/%s/days/%d/skiers/%d",
	                        		10, 2022, 2, 2020)))
	                        .header("Content-Type", "application/json")
	                        .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"time\": %d, \"liftID\": %d}", 217, 17)))
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
	    


}
