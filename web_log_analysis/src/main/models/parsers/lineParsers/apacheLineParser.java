package models.parsers.lineParsers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.exceptions.*;
import models.logData.logData;
import models.utils.ip2Location;
import models.utils.parsedValueClassConverter;

public class apacheLineParser {
    private static parsedValueClassConverter pVCC = new parsedValueClassConverter();
    private static ip2Location ipParser = new ip2Location();
    private static final String regex = "^" + "(?<RemoteIp>-|(?:^|\\b)(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){3})\\s-\\s(?<RemoteUser>-|[a-z_][a-z0-9_]{0,30})\\s(\\[(?<DateTime>(?<Date>[0-2][0-9]\\/\\w{3}\\/[12]\\d{3}):(?<Time>\\d{2}:\\d{2}:\\d{2})[^\\]]*+)\\])\\s(\\\"(?<Request>(?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<RequestURL>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))\\\")\\s(?<Response>-|\\d{3})\\s(?<Bytes>-|\\d+)\\s\\\"(?<Referrer>[^\\s]+)\\\"\\s\\\"(?<UserAgent>[^\\\"]*+)\\\"(?:\\s\\\"(?<ForwardFor>[^\\\"]*+)\\\")?" + "$";
    private static final Pattern pattern = Pattern.compile(regex);
    private static final Matcher matcher = pattern.matcher("");

    public logData parse(String line) throws lineParserException {
        try {
            
            matcher.reset(line);
            
            HashMap<Integer, String> groupNameMapping = new HashMap<Integer, String>();
            for(Map.Entry<String, Integer> tmp: matcher.namedGroups().entrySet())
            {
                groupNameMapping.put(tmp.getValue(), tmp.getKey());
            }

            HashMap<String, Object> parsedData = new HashMap<String, Object>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    parsedData.put(groupNameMapping.get(i), matcher.group(i));
                }
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("time", parsedData.get("DateTime"));
            map.put("remote_ip", parsedData.get("RemoteIp"));
            map.put("remote_user", parsedData.get("RemoteUser"));
            map.put("request", parsedData.get("Request"));
            map.put("response", parsedData.get("Response"));
            map.put("bytes", parsedData.get("Bytes"));
            map.put("referrer", parsedData.get("Referrer"));
            map.put("agent", parsedData.get("UserAgent"));
            map.put("request_method", parsedData.get("RequestMethod"));
            map.put("request_url", parsedData.get("RequestURL"));
            map.put("http_ver", parsedData.get("HttpVer"));
            map.put("geo_info", ipParser.parse(map.get("remote_ip").toString()));

            map = pVCC.fix(map);

            logData res = new logData(map);

            return res;
        } catch (Exception e) {
            throw new lineParserException();
        }
    }
}
