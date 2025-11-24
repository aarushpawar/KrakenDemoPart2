public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1_000];
        long[] totals = new long[1000];

        for (int i = 0; i < threads.length; i++) {
            final int index = i; // required to use i inside lambda function

            threads[i] = new Thread(() -> {
                long total = 0;

                for (int n = 1; n <= 1_000_000; n++) total += n;

                totals[index] = total;
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long grandTotal = 0;
        for (long t : totals) {
            grandTotal += t;
        }

        System.out.println("Grand total of all threads = " + grandTotal);
    }
}