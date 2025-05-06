package main;

import models.*;
import models.exceptions.*;
import models.logData.logData;
import models.mongoDB.mongoDB;
import models.parsers.ResultAggregator;
import models.parsers.fileParsers.*;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import com.mongodb.client.FindIterable;

public class Main {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    private static Logger InfoLogging = Logger.getLogger("InfoLogging");
    private static Logger ErrorLogging = Logger.getLogger("ErrorLogging");

    public static void main(String[] args) {
        try {
            loadProperties(Thread.currentThread().getContextClassLoader().getResource("mongodb_config.properties").getPath());

            long startTime = System.nanoTime();
            ResultAggregator log_data = nFP.parse(Thread.currentThread().getContextClassLoader().getResource("resources/logs_sample/lite/nginx_json_logs_lite.log").getPath());
            //ResultAggregator log_data = nFP.parse("./web_log_analysis/src/main/resources/logs_sample/full/nginx_json_logs_full.log");
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
            System.out.println(log_data.getCount());

            // =============================================

            HashMap<String, Object> filter_rules = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            Date date = sdf.parse("17/May/2015:08:05:23 +0000");

            filter_rules.put("byPeriod", true);
            filter_rules.put("byPeriodStartValue", date);

            // ================= Filter =====================
            mongoDB mongodb = new mongoDB();
            int cnt = 0;
            FindIterable<logData> x = mongodb.filter(filter_rules);
            for (logData doc : x) {
                // Do something here
                //System.out.println(doc.toString());
                cnt += 1;
            }
            System.out.println(cnt);

            // ================= Count =====================
            System.out.println(mongodb.count(filter_rules));

            // for(logData obj : log_data.getAllLogData()) {
            //     System.out.println("Time" + " : " + obj.getTime());
            //     System.out.println("Remote Ip" + " : " + obj.getRemoteIp());
            //     System.out.println("Remote User" + " : " + obj.getRemoteUser());
            //     System.out.println("Request" + " : " + obj.getRequest());
            //     System.out.println("Response Status Code" + " : " + obj.getResponseStatusCode());
            //     System.out.println("Bytes" + " : " + obj.getBytes());
            //     System.out.println("Referrer" + " : " + obj.getReferrer());
            //     System.out.println("Agent" + " : " + obj.getAgent());
            //     System.out.println("Request Method" + " : " + obj.getRequestMethod());
            //     System.out.println("Request URL" + " : " + obj.getByRequestUrl());
            //     System.out.println("Http Ver" + " : " + obj.getHttpVer());
            //     System.out.println("=====================================");
            // }
            // System.out.println("=====================================");
            // System.out.println("=====================================");

            // HashMap<String, Object> filter_rules = new HashMap<String, Object>();
            // filter_rules.put("byRequestMethod", true);
            // filter_rules.put("byRequestMethodValue", "GET");

            // logDataArray filteredByRequestMethod = log_data.filter(filter_rules);

            // for(logData obj : filteredByRequestMethod.getAllLogData()) {
            //     System.out.println("Time" + " : " + obj.getTime());
            //     System.out.println("Remote Ip" + " : " + obj.getRemoteIp());
            //     System.out.println("Remote User" + " : " + obj.getRemoteUser());
            //     System.out.println("Request" + " : " + obj.getRequest());
            //     System.out.println("Response Status Code" + " : " + obj.getResponseStatusCode());
            //     System.out.println("Bytes" + " : " + obj.getBytes());
            //     System.out.println("Referrer" + " : " + obj.getReferrer());
            //     System.out.println("Agent" + " : " + obj.getAgent());
            //     System.out.println("Request Method" + " : " + obj.getRequestMethod());
            //     System.out.println("Request URL" + " : " + obj.getByRequestUrl());
            //     System.out.println("Http Ver" + " : " + obj.getHttpVer());
            //     System.out.println("=====================================");
            // }
            // System.out.println("=====================================");
            // System.out.println("=====================================");
            // System.out.println(log_data.getAllLogData().size());
            // System.out.println(filteredByRequestMethod.getAllLogData().size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void loadProperties(String filePath) throws propertiesLoaderException {
        try (FileInputStream input = new FileInputStream(filePath)) {
            Properties props = new Properties();
            props.load(input);
            
            // Set properties into system properties
            System.getProperties().putAll(props);

            System.out.println("Properties loaded into System properties!");
        } catch (Exception e) {
            throw new propertiesLoaderException();
        }
    }
}