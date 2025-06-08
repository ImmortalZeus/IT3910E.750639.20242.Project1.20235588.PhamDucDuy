package models.parsers.fileParsers;

import models.exceptions.*;
import models.logData.logData;
import models.parsers.ResultAggregator;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class nginxFileParser {
    private static final int BATCH_SIZE = 100;

    public ResultAggregator parse(String filepath) throws fileParserException {
        try {
            // ExecutorService executor = Executors.newFixedThreadPool(
            //     Runtime.getRuntime().availableProcessors()
            // );

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            ResultAggregator aggregator = new ResultAggregator(true, filepath);

            File file = null;
            FileReader fr = null;
            BufferedReader br = null;
            try {
                file = new File(filepath);
                if(!file.exists() || !file.isFile()) {
                    // do something here
                    throw new fileReaderException();
                } else {
                    fr = new FileReader(file);
                    br = new BufferedReader(fr);
                    List<SimpleEntry<Integer, String>> batch = new ArrayList<>(BATCH_SIZE);
                    Integer cnt = 1;
                    String line = br.readLine().trim();
                    while (line != null) {
                        if(line.length() > 0)
                        {
                            batch.add(new SimpleEntry<>(cnt, line));
                            cnt += 1;
                            if (batch.size() >= BATCH_SIZE)
                            {
                                executor.submit(new nginxLineParser(new ArrayList<>(batch), aggregator));
                                batch.clear();
                            }
                        }
                        line = br.readLine();
                    }
                    if (!batch.isEmpty()) {
                        executor.submit(new nginxLineParser(new ArrayList<>(batch), aggregator));
                    }
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.HOURS);
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
                    aggregator.saveToMongodb();
                    return aggregator;
                }
            } catch (Exception e) {
                try {
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
                } catch(Exception e2) {
                }
                throw new fileReaderException();
            } finally {
                try {
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
                } catch(Exception e2) {
                }
            }
        } catch (Exception e) {
            throw new fileParserException();
        }
    }
}