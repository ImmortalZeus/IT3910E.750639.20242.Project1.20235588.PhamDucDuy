package main;

import lineParsers.*;
import logData.*;
import logData.logData;
import fileParsers.*;
import exceptions.*;
import utils.*;

import java.util.logging.Logger;

public class Main {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    private static Logger InfoLogging = Logger.getLogger("InfoLogging");
    private static Logger ErrorLogging = Logger.getLogger("ErrorLogging");

    public static void main(String[] args) {
        try {
            logDataArray log_data = aFP.parse("./web_log_analysis/src/main/resources/logs_sample/full/apache_logs_full.log");
            for(logData obj : log_data.getAllLogData()) {
                System.out.println("Time" + " : " + obj.getTime());
                System.out.println("Remote Ip" + " : " + obj.getRemoteIp());
                System.out.println("Remote User" + " : " + obj.getRemoteUser());
                System.out.println("Request" + " : " + obj.getRequest());
                System.out.println("Response" + " : " + obj.getResponse());
                System.out.println("Bytes" + " : " + obj.getBytes());
                System.out.println("Referrer" + " : " + obj.getReferrer());
                System.out.println("Agent" + " : " + obj.getAgent());
                System.out.println("Request Method" + " : " + obj.getRequestMethod());
                System.out.println("Request URL" + " : " + obj.getRequestURL());
                System.out.println("Http Ver" + " : " + obj.getHttpVer());
                System.out.println("=====================================");
            }
            System.out.println("=====================================");
            for(logData obj : log_data.getByRequestMethod("GET").getAllLogData()) {
                System.out.println("Time" + " : " + obj.getTime());
                System.out.println("Remote Ip" + " : " + obj.getRemoteIp());
                System.out.println("Remote User" + " : " + obj.getRemoteUser());
                System.out.println("Request" + " : " + obj.getRequest());
                System.out.println("Response" + " : " + obj.getResponse());
                System.out.println("Bytes" + " : " + obj.getBytes());
                System.out.println("Referrer" + " : " + obj.getReferrer());
                System.out.println("Agent" + " : " + obj.getAgent());
                System.out.println("Request Method" + " : " + obj.getRequestMethod());
                System.out.println("Request URL" + " : " + obj.getRequestURL());
                System.out.println("Http Ver" + " : " + obj.getHttpVer());
                System.out.println("=====================================");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}