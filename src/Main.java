import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        ArrayList<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            futures.add(executor.submit(new Counter()));
        }


        executor.shutdown();
    }
}