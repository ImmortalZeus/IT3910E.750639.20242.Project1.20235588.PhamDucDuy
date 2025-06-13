package parserTest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ua_parser.Parser;
import ua_parser.Client;

public class Test {
    private static final int BATCH_SIZE = 100;

    public static void main(String[] args) {
        try {
            System.out.println("");
            long startTime = System.currentTimeMillis();
            Parser parser = new Parser();
            Client client = parser.parse("-");
            System.out.println(client);
            long stopTime = System.currentTimeMillis();
            System.out.println(stopTime - startTime);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
