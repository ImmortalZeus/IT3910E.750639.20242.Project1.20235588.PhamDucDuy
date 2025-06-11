package models.parsers.lineParsers;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ip2location.IPResult;

import models.exceptions.*;
import models.logData.logData;
import models.mongoDB.mongoDB;
import models.parsers.ResultAggregator;
import models.utils.ip2Location;
import models.utils.parsedValueClassConverter;
import models.utils.userAgentParser;

import ua_parser.Parser;
import ua_parser.Client;

public class apacheLineParser implements Runnable {
    private static mongoDB mongodb = new mongoDB();
    private static final String regex = "^" + "(?<RemoteIp>-|(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){3})\\s(?<RemoteLogName>-|\\S+)\\s(?<RemoteUser>-|\\S+)\\s(\\[(?<DateTime>(?<Date>\\d{2})\\/\\w{3}\\/\\d{4}:(?<Time>\\d{2}:\\d{2}:\\d{2})\\s(?<Timezone>[+-]\\d{4}))\\])\\s(\\\"(?<Request>(?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<RequestUrl>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))\\\")\\s(?<Response>-|\\d{3})\\s(?<Bytes>-|\\d+)\\s\\\"(?<Referrer>[^\\s]+)\\\"\\s\\\"(?<UserAgent>[^\\\"]*+)\\\"(?:\\s\\\"(?<ForwardFor>[^\\\"]*+)\\\")?" + "$";
    private static final Pattern pattern = Pattern.compile(regex);
    private ResultAggregator aggregator;
    private List<SimpleEntry<Integer, String>> lines;
    public apacheLineParser(List<SimpleEntry<Integer, String>> lines, ResultAggregator agg) {
        this.lines = lines;
        this.aggregator = agg;
    }
    @Override
    public final void run() {
        try {
            Matcher matcher = pattern.matcher("");
            HashMap<Integer, String> groupNameMapping = new HashMap<Integer, String>();
            //HashMap<String, Object> parsedData = new HashMap<String, Object>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (SimpleEntry<Integer, String> line : lines) {
                try {
                    map.clear();
                    matcher.reset(line.getValue());
                    if (matcher.matches()) {
                        map.put("time", matcher.group(5));
                        map.put("remote_ip", matcher.group(1));
                        map.put("remote_user", matcher.group(3));
                        map.put("request", matcher.group(10));
                        map.put("response", matcher.group(14));
                        map.put("bytes", matcher.group(15));
                        map.put("referrer", matcher.group(16));
                        map.put("agent", matcher.group(17));
                        map.put("request_method", matcher.group(11));
                        map.put("request_url", matcher.group(12));
                        map.put("http_ver", matcher.group(13));
                        try {
                            IPResult ipResult = ip2Location.parse(map.get("remote_ip").toString());
                            map.put("country_short", ipResult.getCountryShort());
                            map.put("country_long", ipResult.getCountryLong());
                            map.put("region", ipResult.getRegion());
                            map.put("city", ipResult.getCity());
                            map.put("zip_code", ipResult.getZipCode());
                            map.put("time_zone", ipResult.getTimeZone());
                        } catch(Exception e) {
                            map.put("country_short", null);
                            map.put("country_long", null);
                            map.put("region", null);
                            map.put("city", null);
                            map.put("zip_code", null);
                            map.put("time_zone", null);
                        }
                        try {
                            String ua = map.get("agent").toString().trim();
                            if(ua != null && !ua.equals("-"))
                            {
                                Client useragentParsed = userAgentParser.parse(ua);
                                map.put("browser", useragentParsed.userAgent.family);
                                map.put("OS", useragentParsed.os.family);
                                map.put("device", useragentParsed.device.family);
                            }
                            else
                            {
                                map.put("browser", null);
                                map.put("OS", null);
                                map.put("device", null);
                            }
                        } catch(Exception e) {
                            map.put("browser", null);
                            map.put("OS", null);
                            map.put("device", null);
                        }
                        map.put("index", line.getKey());
                        map = parsedValueClassConverter.fix(map);
            
                        logData res = new logData(map);
                        //mongodb.insertOne(res);
                        this.aggregator.collect(res);
                        this.aggregator.addSucceed();
                    }
                    else
                    {
                        this.aggregator.addFail();
                    }
                } catch (Exception e) {
                    this.aggregator.addFail();
                }
            }
        } catch (Exception e) {
            // this.aggregator.addFail();
        }
    }
}
