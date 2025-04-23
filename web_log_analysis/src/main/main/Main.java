package main;

import models.*;
import models.exceptions.*;
import models.logData.logDataArray;
import models.parsers.ResultAggregator;
import models.parsers.fileParsers.*;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.util.HashMap;
import java.util.logging.Logger;

public class Main {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    private static Logger InfoLogging = Logger.getLogger("InfoLogging");
    private static Logger ErrorLogging = Logger.getLogger("ErrorLogging");

    public static void main(String[] args) {
        try {
            System.out.println("");
            long startTime = System.nanoTime();
            ResultAggregator log_data = nFP.parse("./web_log_analysis/src/main/resources/logs_sample/large/nginx_json_logs_large.log");
            //ResultAggregator log_data = nFP.parse("./web_log_analysis/src/main/resources/logs_sample/full/nginx_json_logs_full.log");
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
            System.out.println(log_data.getCountByCountryShort());
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
            //     System.out.println("Request URL" + " : " + obj.getRequestURL());
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
            //     System.out.println("Request URL" + " : " + obj.getRequestURL());
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
}