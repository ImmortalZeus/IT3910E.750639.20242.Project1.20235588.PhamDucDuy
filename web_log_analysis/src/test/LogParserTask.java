import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.*;

public class LogParserTask implements Runnable {
    private static final String LOG_PATTERN = "(?<RemoteIp>-|(?:^|\\b(?<!\\.))(?:1?\\d\\d?|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d\\d?|2[0-4]\\d|25[0-5])){3}(?=$|[^\\w.]))\\s-\\s(?<RemoteUser>-|[a-z_][a-z0-9_]{0,30})\\s(\\[(?<DateTime>(?<Date>[0-2][0-9]\\/\\w{3}\\/[12]\\d{3}):(?<Time>\\d\\d:\\d\\d:\\d\\d).*)\\])\\s(\\\"(?<Request>(?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<RequestURL>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))\\\")\\s(?<Response>-|\\d{3})\\s(?<Bytes>-|\\d+)\\s\\\"(?<Referrer>[^\\s]+)\\\"\\s\\\"(?<UserAgent>[^\\\"]+)\\\"(?:\\s\\\"(?<ForwardFor>[^\\\"]+)\\\")?";
    private static final Pattern PATTERN = Pattern.compile(LOG_PATTERN);
    private final ResultAggregator aggregator;
    private final List<String> lines;
    
    public LogParserTask(List<String> lines, ResultAggregator agg) {
        this.lines = lines;
        this.aggregator = agg;
    }
    @Override
    public void run() {
        for (String line : lines) {
            Matcher m = PATTERN.matcher(line);
            // int i = 0;
            // while (m.find()) {
            //    for (int j = 0; j <= m.groupCount(); j++) {
                //       System.out.println("------------------------------------");
            //       System.out.println("Group " + i + ": " + m.group(j));
            //       i++;
            //    }
            // }
            
            if (m.matches()) {
                
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
                    LogEntry entry = new LogEntry(
                        m.group(1),
                        m.group(2),
                        sdf.parse((String) m.group(4)),
                        m.group(8),
                        Integer.parseInt(m.group(12)),
                        "-".equals(m.group(13)) ? 0 : Long.parseLong(m.group(13))
                        );
                    aggregator.collect(entry);
                } catch (Exception e) {
                    // optionally log or count parse errors
                }
            } else {
                // optionally count malformed lines
            }
        }
    }
}
