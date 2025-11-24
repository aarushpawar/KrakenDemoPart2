import java.util.concurrent.Callable;

public class Counter implements Callable<Long> {
    @Override
    public Long call() {
        long total = 0;
        for (int n = 1; n <= 1_000_000; n++) {
            total += n;
        }
        return total;
    }
}