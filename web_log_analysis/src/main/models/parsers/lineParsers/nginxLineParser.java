package models.parsers.lineParsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ip2location.IPResult;

import models.logData.logData;
import models.mongoDB.mongoDB;
import models.parsers.ResultAggregator;
import models.exceptions.*;
import models.utils.ip2Location;
import models.utils.parsedValueClassConverter;
import models.utils.userAgentParser;

import ua_parser.Client;
import ua_parser.Parser;

public class nginxLineParser implements Runnable {
    private static mongoDB mongodb = new mongoDB();
    private static ObjectMapper mapper = new ObjectMapper();
    private static final String regex = "((?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<ByRequestUrl>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))";
    private static final Pattern pattern = Pattern.compile(regex);
    private ResultAggregator aggregator;
    private List<SimpleEntry<Integer, String>> lines;
    public nginxLineParser(List<SimpleEntry<Integer, String>> lines, ResultAggregator agg) {
        this.lines = lines;
        this.aggregator = agg;
        // mapper.disable(MapperFeature.AUTO_DETECT_CREATORS);
        // mapper.disable(MapperFeature.AUTO_DETECT_FIELDS);
        // mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
    }
    @Override
    public void run() {
        try {
            Matcher matcher = pattern.matcher("");
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (SimpleEntry<Integer, String> line : lines) {
                map.clear();
                map = mapper.readValue(line.getValue(), new TypeReference<HashMap<String, Object>>() {});

                String request = (String) map.get("request");
                matcher.reset(request);
    
                if (matcher.matches()) {
                    map.put("request_method", matcher.group(2));
                    map.put("request_url", matcher.group(3));
                    map.put("http_ver", matcher.group(4));
                    try {
                        IPResult ipResult = ip2Location.parse(map.get("remote_ip").toString());
                        map.put("country_short", ipResult.getCountryShort());
                        map.put("country_long", ipResult.getCountryLong());
                        map.put("region", ipResult.getRegion());
                        map.put("city", ipResult.getCity());
                        map.put("zip_code", ipResult.getZipCode());
                        map.put("time_zone", ipResult.getTimeZone());
                    } catch(Exception e) {
                        map.put("country_short", "-");
                        map.put("country_long", "-");
                        map.put("region", "-");
                        map.put("city", "-");
                        map.put("zip_code", "-");
                        map.put("time_zone", "-");
                    }
                    try {
                        String ua = map.get("agent").toString().trim();
                        if(ua != null && ua != "-")
                        {
                            Client useragentParsed = userAgentParser.parse(ua);
                            map.put("browser", useragentParsed.userAgent.family);
                            map.put("OS", useragentParsed.os.family);
                            map.put("device", useragentParsed.device.family);
                        }
                        else
                        {
                            map.put("browser", "-");
                            map.put("OS", "-");
                            map.put("device", "-");
                        }
                    } catch(Exception e) {
                        map.put("browser", "-");
                        map.put("OS", "-");
                        map.put("device", "-");
                    }
                }
                map.put("index", line.getKey());
                map = parsedValueClassConverter.fix(map);
                
                logData res = new logData(map);
                //mongodb.insertOne(res);    
                this.aggregator.collect(res);
                this.aggregator.addSucceed();
                // HashMap<String, String> res = new HashMap<String, String>();
                // for(Map.Entry<String, Object> tmp: map.entrySet())
                // {
                //     res.put(tmp.getKey(), tmp.getValue().toString());
                // }
                // return res;
            }
        } catch (Exception e) {
            this.aggregator.addFail();
        }
    }
}