package models.parsers.fileParsers;

import models.exceptions.*;
import models.logData.logData;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class apacheFileParser {
    private static final int BATCH_SIZE = 100;

    public void parse(String filepath) throws fileParserException {
        try {
            // ExecutorService executor = Executors.newFixedThreadPool(
            //     Runtime.getRuntime().availableProcessors()
            // );

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();


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
                    List<String> batch = new ArrayList<>(BATCH_SIZE);
                    String line = br.readLine().trim();
                    while (line != null) {
                        if(line.length() > 0)
                        {
                            batch.add(line);
                            if (batch.size() >= BATCH_SIZE)
                            {
                                executor.submit(new apacheLineParser(new ArrayList<>(batch)));
                                batch.clear();
                            }
                        }
                        line = br.readLine();
                    }
                    if (!batch.isEmpty()) {
                        executor.submit(new apacheLineParser(new ArrayList<>(batch)));
                    }
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.HOURS);
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
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
            }
        } catch (Exception e) {
            throw new fileParserException();
        }
    }
}
