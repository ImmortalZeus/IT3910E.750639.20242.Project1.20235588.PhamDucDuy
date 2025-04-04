package lineParsers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.*;

public class apacheLineParser {
    private static final String regex = "(?<RemoteIp>(?:^|\\b(?<!\\.))(?:1?\\d\\d?|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d\\d?|2[0-4]\\d|25[0-5])){3}(?=$|[^\\w.]))\\s-\\s(?<RemoteUser>-|[a-z_][a-z0-9_]{0,30})\\s(\\[(?<DateTime>(?<Date>[0-2][0-9]\\/\\w{3}\\/[12]\\d{3}):(?<Time>\\d\\d:\\d\\d:\\d\\d).*)\\])\\s(\\\"(?<Request>(?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<RequestURL>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))\\\")\\s(?<Response>-|\\d{3})\\s(?<Bytes>-|\\d+)\\s\\\"(?<Referrer>[^\\s]+)\\\"\\s\\\"(?<UserAgent>[^\\\"]+)\\\"(?:\\s\\\"(?<ForwardFor>[^\\\"]+)\\\")?";
    public HashMap<String, String> parse(String line) throws lineParserException {
        try {
            
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(line);
            
            HashMap<Integer, String> groupNameMapping = new HashMap<Integer, String>();
            for(Map.Entry<String, Integer> tmp: matcher.namedGroups().entrySet())
            {
                groupNameMapping.put(tmp.getValue(), tmp.getKey());
            }

            HashMap<String, String> parsedData = new HashMap<String, String>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    parsedData.put(groupNameMapping.get(i), matcher.group(i));
                }
            }
            HashMap<String, String> res = new HashMap<String, String>();
            res.put("time", parsedData.get("DateTime"));
            res.put("remote_ip", parsedData.get("RemoteIp"));
            res.put("remote_user", parsedData.get("RemoteUser"));
            res.put("request", parsedData.get("Request"));
            res.put("response", parsedData.get("Response"));
            res.put("bytes", parsedData.get("Bytes"));
            res.put("referrer", parsedData.get("Referrer"));
            res.put("agent", parsedData.get("UserAgent"));
            return res;
        } catch (Exception e) {
            throw new lineParserException();
        }
    }
}
