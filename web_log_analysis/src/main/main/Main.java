package main;

import lineParsers.*;
import fileParsers.*;
import exceptions.*;
import utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    private static Logger InfoLogging = Logger.getLogger("InfoLogging");
    private static Logger ErrorLogging = Logger.getLogger("ErrorLogging");

    public static void main(String[] args) {
        try {
            ArrayList<HashMap<String, String>> log_data = aFP.parse("./web_log_analysis/src/main/resources/logs_sample/full/apache_logs_full.log");
            for(HashMap<String, String> obj : log_data) {
                for(String k : obj.keySet())
                {
                    System.out.println(k + " : " + obj.get(k));
                }
                System.out.println("=====================================");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}