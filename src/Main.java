import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {

    private static final int NUM_TASKS = 1000;
    private static final int NUM_RUNS = 100;

    public static int threads = 1;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Running ExecutorService benchmark (1 thread)...");
        double avgExecutorTime1Thread = averageTime(Main::runExecutorBenchmark, NUM_RUNS);

        threads = 2;
        System.out.println("Running ExecutorService benchmark (2 threads)...");
        double avgExecutorTime2Thread = averageTime(Main::runExecutorBenchmark, NUM_RUNS);

        threads = 4;
        System.out.println("Running ExecutorService benchmark (4 threads)...");
        double avgExecutorTime4Thread = averageTime(Main::runExecutorBenchmark, NUM_RUNS);

        threads = 8;
        System.out.println("Running ExecutorService benchmark (8 threads)...");
        double avgExecutorTime8Thread = averageTime(Main::runExecutorBenchmark, NUM_RUNS);

        threads = 16;
        System.out.println("Running ExecutorService benchmark (16 threads)...");
        double avgExecutorTime16Thread = averageTime(Main::runExecutorBenchmark, NUM_RUNS);

        System.out.println("Running Regular Threads benchmark...");
        double avgThreadTime = averageTime(Main::runThreadBenchmark, NUM_RUNS);

        System.out.println("Running Single-threaded benchmark...");
        double avgSingleTime = averageTime(Main::runSingleThreadedBenchmark, NUM_RUNS);

        System.out.printf("Average time (ExecutorService [1]) over %d runs: %.2f ms%n%n", NUM_RUNS, avgExecutorTime1Thread);
        System.out.printf("Average time (ExecutorService [2]) over %d runs: %.2f ms%n%n", NUM_RUNS, avgExecutorTime2Thread);
        System.out.printf("Average time (ExecutorService [4]) over %d runs: %.2f ms%n%n", NUM_RUNS, avgExecutorTime4Thread);
        System.out.printf("Average time (ExecutorService [8]) over %d runs: %.2f ms%n%n", NUM_RUNS, avgExecutorTime8Thread);
        System.out.printf("Average time (ExecutorService [16]) over %d runs: %.2f ms%n%n", NUM_RUNS, avgExecutorTime16Thread);
        System.out.printf("Average time (Regular Threads) over %d runs: %.2f ms%n%n", NUM_RUNS, avgThreadTime);
        System.out.printf("Average time (Single-threaded) over %d runs: %.2f ms%n", NUM_RUNS, avgSingleTime);
    }

    private static void runExecutorBenchmark() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        ArrayList<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < NUM_TASKS; i++) {
            futures.add(executor.submit(new Counter()));
        }

        long grandTotal = 0;
        for (Future<Long> f : futures) {
            grandTotal += f.get();
        }

        long end = System.currentTimeMillis();
        System.out.println("ExecutorService grand total = " + grandTotal);
        System.out.println("Time taken (executor-service): " + (end - start) + " ms");

        executor.shutdown();
    }

    private static double averageTime(Benchmark benchmark, int runs) throws InterruptedException, ExecutionException {
        long totalTime = 0;
        for (int i = 0; i < runs; i++) {
            long start = System.currentTimeMillis();
            benchmark.run();
            long end = System.currentTimeMillis();
            totalTime += (end - start);
        }
        return (double) totalTime / runs;
    }

    // Functional interface for benchmarking
    @FunctionalInterface
    interface Benchmark {
        void run() throws InterruptedException, ExecutionException;
    }

    private static void runThreadBenchmark() throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[NUM_TASKS];
        long[] totals = new long[NUM_TASKS];

        for (int i = 0; i < NUM_TASKS; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                long total = 0;
                for (int n = 1; n <= 1_000_000; n++) total += n;
                totals[index] = total;
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long grandTotal = 0;
        for (long t : totals) grandTotal += t;

        long end = System.currentTimeMillis();
        System.out.println("Regular threads grand total = " + grandTotal);
        System.out.println("Time taken (regular threads): " + (end - start) + " ms");
    }

    private static void runSingleThreadedBenchmark() {
        long start = System.currentTimeMillis();

        long grandTotal = 0;
        for (int i = 0; i < NUM_TASKS; i++) {
            long total = 0;
            for (int n = 1; n <= 1_000_000; n++) total += n;
            grandTotal += total;
        }

        long end = System.currentTimeMillis();
        System.out.println("Single-threaded grand total = " + grandTotal);
        System.out.println("Time taken (single-threaded): " + (end - start) + " ms");
    }
}