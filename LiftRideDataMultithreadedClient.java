/*This Java code is an implementation of a multithreaded client for sending HTTP POST requests to a 
server at a given URL with a payload of lift ride data. The lift ride data consists of information about a 
skier's ride on a lift, including the skier ID, resort ID, season ID, day ID, lift ID, and time of the ride.*/ 







import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LiftRideDataMultithreadedClient {
    
    private static final String BASE_URL = "http://localhost:8080/LiftRides/liftrides";
    private static final int NUM_THREADS = 32;
    private static final int POSTS_PER_THREAD = 1000;
    private static final int NUM_POSTS = NUM_THREADS * POSTS_PER_THREAD;
    private static final int SKIER_ID_MIN = 1;
    private static final int SKIER_ID_MAX = 100000;
    private static final int RESORT_ID_MIN = 1;
    private static final int RESORT_ID_MAX = 10;
    private static final int LIFT_ID_MIN = 1;
    private static final int LIFT_ID_MAX = 40;
    private static final int SEASON_ID = 2022;
    private static final int DAY_ID = 1;
    private static final int TIME_MIN = 1;
    private static final int TIME_MAX = 360;
    
    private static final Random random = new Random();
    private static final ConcurrentLinkedQueue<String> liftRideQueue = new ConcurrentLinkedQueue<>();
    
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Create a single thread to generate lift ride events
        Runnable liftRideGenerator = () -> {
            for (int i = 0; i < NUM_POSTS; i++) {
                String liftRideEvent = generateLiftRideEvent();
                liftRideQueue.offer(liftRideEvent); // add lift ride event to the queue
            }
        };
        new Thread(liftRideGenerator).start();
        
        // Create 32 threads that each send 1000 POST requests
        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < POSTS_PER_THREAD; j++) {
                    String liftRideEvent = liftRideQueue.poll();
                    while (liftRideEvent == null) { // wait for lift ride event to become available
                        liftRideEvent = liftRideQueue.poll();
                    }
                    sendPostRequest(liftRideEvent);
                }
            });
        }
        
        // Wait for all threads to finish
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
    
    private static String generateLiftRideEvent() {
        int skierID = random.nextInt(SKIER_ID_MAX - SKIER_ID_MIN + 1) + SKIER_ID_MIN;
        int resortID = random.nextInt(RESORT_ID_MAX - RESORT_ID_MIN + 1) + RESORT_ID_MIN;
        int liftID = random.nextInt(LIFT_ID_MAX - LIFT_ID_MIN + 1) + LIFT_ID_MIN;
        int time = random.nextInt(TIME_MAX - TIME_MIN + 1) + TIME_MIN;
        return String.format("%d,%d,%d,%d,%d,%d", skierID, resortID, SEASON_ID, DAY_ID, liftID, time);
    }
    
    private static void sendPostRequest(String liftRideEvent) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(liftRideEvent))
                    .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode != 201) {
            System.err.println("Failed to send POST request. Status code: " + statusCode);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
