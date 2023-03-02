/*Multithreaded concurrent req 


This implementation generates 10,000 lift ride events and stores them in a ConcurrentLinkedQueue.
It then creates 32 threads that each consume lift ride events from the queue and send them to the server using the LiftRideDataClient.sendPostRequest() method. 
Once all 10,000 events have been uploaded, the main thread outputs the total time taken and the average events per second.*/

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LiftRideEventGenerator implements Runnable {
    private static final int NUM_EVENTS = 10000;
    private static final int NUM_THREADS = 32;
    private static final int EVENTS_PER_THREAD = NUM_EVENTS / NUM_THREADS;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final ConcurrentLinkedQueue<String> EVENT_QUEUE = new ConcurrentLinkedQueue<>();

    private static void generateEvents() {
        for (int i = 0; i < NUM_EVENTS; i++) {
            int skierID = (int) (Math.random() * 100000) + 1;
            int resortID = (int) (Math.random() * 10) + 1;
            int liftID = (int) (Math.random() * 40) + 1;
            int seasonID = 2022;
            int dayID = 1;
            int time = (int) (Math.random() * 360) + 1;
            LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 0, 0).plusMinutes(time);
            String event = String.format("%d,%d,%d,%d,%d,%s", skierID, resortID, seasonID, dayID, liftID,
                    TIME_FORMATTER.format(dateTime));
            EVENT_QUEUE.add(event);
        }
    }

    public void run() {
        while (!EVENT_QUEUE.isEmpty()) {
            String event = EVENT_QUEUE.poll();
            if (event == null) {
                continue;
            }
            try {
                LiftRideDataClient.sendPostRequest(event);
            } catch (Exception e) {
                System.err.println("Error sending POST request: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        generateEvents();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new LiftRideEventGenerator());
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].start();
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Uploaded %d lift ride events in %d ms (%.2f events/s)%n", NUM_EVENTS, endTime - startTime,
                (double) NUM_EVENTS / ((double) (endTime - startTime) / 1000.0));
    }
}
