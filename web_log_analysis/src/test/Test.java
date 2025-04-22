import models.logData.logDataArray;
import models.parsers.fileParsers.apacheFileParser;
import models.parsers.fileParsers.nginxFileParser;
import models.utils.fileReader;
import models.utils.ip2Location;

public class Test {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    public static void main(String[] args) {
        try {
            System.out.println("");
            long startTime = System.nanoTime();
            logDataArray log_data = aFP.parse("./web_log_analysis/src/main/resources/logs_sample/large/apache_logs_large.log");
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
