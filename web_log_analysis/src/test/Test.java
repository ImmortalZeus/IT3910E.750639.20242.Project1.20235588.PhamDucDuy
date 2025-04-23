import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
    private static final int BATCH_SIZE = 100;

    public static void main(String[] args) {
        try {
            System.out.println("");
            long startTime = System.nanoTime();
            String filePath = "./web_log_analysis/src/main/resources/logs_sample/large/apache_logs_large.log";
            ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
            );
            ResultAggregator aggregator = new ResultAggregator();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                List<String> batch = new ArrayList<>(BATCH_SIZE);
                String line;
                while ((line = reader.readLine()) != null) {
                    batch.add(line);
                    if (batch.size() >= BATCH_SIZE) {
                        executor.submit(new LogParserTask(new ArrayList<>(batch), aggregator));
                        batch.clear();
                    }
                }
                if (!batch.isEmpty()) {
                    executor.submit(new LogParserTask(batch, aggregator));
                }
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            // All parsing done-print or write out aggregated results
            aggregator.report();
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
