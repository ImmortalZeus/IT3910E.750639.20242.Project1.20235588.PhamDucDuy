package parserTest;
import java.util.concurrent.atomic.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultAggregator {
    private final AtomicLong totalBytes = new AtomicLong();
    private final AtomicLong count = new AtomicLong();
    private final Map<Integer, AtomicLong> statusCounts = new ConcurrentHashMap<>();

    public void collect(LogEntry e) {
        totalBytes.addAndGet(e.getBytes());
        count.incrementAndGet();
        statusCounts
            .computeIfAbsent(e.getStatus(), k -> new AtomicLong())
            .incrementAndGet();
    }

    public void report() {
        System.out.println("Parsed lines: " + count.get());
        System.out.println("Total bytes served: " + totalBytes.get());
        statusCounts.forEach((status, c) ->
            System.out.println("Status " + status + ": " + c.get())
        );
    }
}
