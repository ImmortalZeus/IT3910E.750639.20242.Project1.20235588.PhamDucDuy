package models.parsers.fileParsers;

import models.exceptions.*;
import models.logData.logData;
import models.parsers.ResultAggregator;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class apacheFileParser {
    private static final Integer BATCH_SIZE = 100;

    public final static ResultAggregator parse(String filepathstr) throws fileParserException {
        if(filepathstr == null) {
            throw new fileParserException();
        }
        Path filepath = null;
        try {
            filepath = Paths.get(filepathstr).toRealPath();
        } catch (IOException x) {
            throw new fileParserException();
        }
        if(filepath == null) {
            throw new fileParserException();
        }
        if(!isSecureFile.check(filepath)) {
            throw new fileParserException();
        }
        try {
            // ExecutorService executor = Executors.newFixedThreadPool(
            //     Runtime.getRuntime().availableProcessors()
            // );
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            ResultAggregator aggregator = new ResultAggregator(true, filepath.toString());

            File file = null;
            File destinationfile = null;
            FileReader fr = null;
            BufferedReader br = null;
            String now = Long.toString(System.currentTimeMillis());
            try {
                file = filepath.toFile();
                if(!file.exists() || !file.isFile()) {
                    // do something here
                    throw new fileReaderException();
                } else {
                    String fileName = filepath.getFileName().toString();
                    String nameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
                    String fileExtension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
                    String appDataPath = System.getenv("APPDATA");
                    Path destination = Paths.get(appDataPath, "web_log_analysis", nameWithoutExt + "-" + now + (fileExtension.isEmpty() ? "" : ("." + fileExtension)));
                    Files.createDirectories(destination.getParent());
                    Files.copy(filepath, destination, StandardCopyOption.REPLACE_EXISTING);
                    destinationfile = destination.toFile();
                    if(!isSecureFile.check(destination) || !destinationfile.exists() || !destinationfile.isFile())
                    {
                        throw new fileParserException();
                    }
                    destinationfile.setReadOnly();

                    fr = new FileReader(destinationfile);
                    br = new BufferedReader(fr);
                    List<SimpleEntry<Integer, String>> batch = new ArrayList<>(BATCH_SIZE);
                    Integer cnt = 1;
                    String line = Normalizer.normalize(br.readLine().trim(), Normalizer.Form.NFKC);
                    while (line != null) {
                        if(line.length() > 0)
                        {
                            batch.add(new SimpleEntry<>(cnt, line));
                            cnt += 1;
                            if (batch.size() >= BATCH_SIZE)
                            {
                                executor.submit(new apacheLineParser(new ArrayList<>(batch), aggregator));
                                batch.clear();
                            }
                        }
                        line = br.readLine();
                    }
                    if (!batch.isEmpty()) {
                        executor.submit(new apacheLineParser(new ArrayList<>(batch), aggregator));
                    }
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.HOURS);
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
                    if(destinationfile != null)
                    {
                        destinationfile.setWritable(true);
                        Files.delete(destinationfile.toPath());
                    }
                    aggregator.saveToMongodb();
                    return aggregator;
                }
            } catch (Exception e) {
                try {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                    if(fr != null) {
                        fr.close();
                    }
                    if(br != null) {
                        br.close();
                    }
                    if(destinationfile != null)
                    {
                        destinationfile.setWritable(true);
                        Files.delete(destinationfile.toPath());
                    }
                } catch(Exception e2) {
                }
                throw new fileParserException();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new fileParserException();
        }
    }
}