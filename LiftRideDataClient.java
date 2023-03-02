/* Build the Client We want a multithreaded Java client we can configure to upload a day of lift rides to the server and 
exert various loads on the server. First you need to get a Java client to call your server APIs. 
Write a simple test that calls the API before proceeding, to establish that you have connectivity. 
To connect to your remote server, you need to call a client method using the Java 11 HTTP client classes or the Apache Java HTTP API. 
Each client per thread in a multithreaded environment to simulate multiple clients in concurrency.*/ 

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LiftRideDataClient {
    private static final String SERVER_URL = "http://localhost:8080/HelloServlet"; // Replace with your server URL
    private static final int THREAD_POOL_SIZE = 10; // Number of threads to use

    public static void main(String[] args) {
        // Call the API to test connectivity
        System.out.println("Testing API connectivity...");
        boolean isConnected = testApiConnectivity();
        if (isConnected) {
            System.out.println("API connectivity test successful!");
        } else {
            System.out.println("API connectivity test failed.");
            return;
        }

        // Generate a day's worth of lift rides
        List<LiftRideData> liftRides = generateLiftRideDataForDay();

        // Split the lift rides into batches for each thread
        int batchSize = liftRides.size() / THREAD_POOL_SIZE;
        List<List<LiftRideData>> batches = new ArrayList<>();
        for (int i = 0; i < THREAD_POOL_SIZE - 1; i++) {
            List<LiftRideData> batch = liftRides.subList(i * batchSize, (i + 1) * batchSize);
            batches.add(batch);
        }
        List<LiftRideData> lastBatch = liftRides.subList((THREAD_POOL_SIZE - 1) * batchSize, liftRides.size());
        batches.add(lastBatch);

        // Create a thread pool to upload the lift rides concurrently
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (List<LiftRideData> batch : batches) {
            executor.execute(() -> {
                uploadLiftRideData(batch);
            });
        }

        // Shut down the thread pool when all tasks are complete
        executor.shutdown();
    }

    // Test API connectivity by calling a test endpoint
    private static boolean testApiConnectivity() {
        try {
            URL url = new URL(SERVER_URL + "/test");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generate a day's worth of lift rides for testing purposes
    private static List<LiftRideData> generateLiftRideDataForDay() {
        List<LiftRideData> liftRides = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 10000; i++) {
            LocalDateTime rideTime = now.minusDays(1).plusMinutes(i * 5);
            String skierId = "skier_" + i;
            String liftId = "lift_" + (i % 10);
            liftRides.add(new LiftRideData(skierId, liftId, rideTime));
        }
        return liftRides;
    }

    // Upload a batch of lift rides to the server
    private static void uploadLiftRideData(List<LiftRideData> liftRides) {
        try {
            URL url = new URL(SERVER_URL + "/lift-rides");
            for (LiftRideData liftRide : liftRides) {
            	String requestBody = liftRide.toJson();
            	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            	conn.setRequestMethod("POST");
            	conn.setRequestProperty("Content-Type", "application/json");
            	conn.setDoOutput(true);
            	conn.getOutputStream().write(requestBody.getBytes());
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                    System.out.println("Failed to upload lift ride: " + requestBody);
                }

                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Data class representing a single lift ride
    private static class LiftRideData {
        private String skierId;
        private String liftId;
        private LocalDateTime rideTime;

        public LiftRideData(String skierId, String liftId, LocalDateTime rideTime) {
            this.skierId = skierId;
            this.liftId = liftId;
            this.rideTime = rideTime;
        }

        public String toJson() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String rideTimeStr = rideTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
            return String.format("{\"skierId\": \"%s\", \"liftId\": \"%s\", \"rideTime\": \"%s\"}", skierId, liftId, rideTimeStr);
        }
    }
}
            	
