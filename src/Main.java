import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        ArrayList<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            futures.add(executor.submit(new Counter()));
        }

        long grandTotal = 0;

        for (Future<Long> f : futures) {
            grandTotal += f.get();
        }

        System.out.println("Grand total of all threads = " + grandTotal);

        executor.shutdown();
        
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }
}