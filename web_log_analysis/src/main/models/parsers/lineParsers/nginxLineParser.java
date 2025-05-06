package models.parsers.lineParsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class nginxLineParser implements Runnable {
    private static parsedValueClassConverter pVCC = new parsedValueClassConverter();
    private static ip2Location ipParser = new ip2Location();
        private static mongoDB mongodb = new mongoDB();
    private static ObjectMapper mapper = new ObjectMapper();
    private static final String regex = "((?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<ByRequestUrl>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))";
    private static final Pattern pattern = Pattern.compile(regex);
    private ResultAggregator aggregator;
    private List<String> lines;
    public nginxLineParser(List<String> lines, ResultAggregator agg) {
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
            for (String line : lines) {
                map.clear();
                map = mapper.readValue(line, new TypeReference<HashMap<String, Object>>() {});

                String request = (String) map.get("request");
                matcher.reset(request);
    
                if (matcher.matches()) {
                    map.put("request_method", matcher.group(2));
                    map.put("request_url", matcher.group(3));
                    map.put("http_ver", matcher.group(4));
                    IPResult ipResult = ipParser.parse(map.get("remote_ip").toString());
                    map.put("country_short", ipResult.getCountryShort());
                    map.put("country_long", ipResult.getCountryLong());
                    map.put("region", ipResult.getRegion());
                    map.put("city", ipResult.getCity());
                    map.put("latitude", ipResult.getLatitude());
                    map.put("longitude", ipResult.getLongitude());
                    map.put("zip_code", ipResult.getZipCode());
                    map.put("time_zone", ipResult.getTimeZone());
                }

                map = pVCC.fix(map);
                
                logData res = new logData(map);
                mongodb.insertOne(res);

                aggregator.collect(res);
    
                // HashMap<String, String> res = new HashMap<String, String>();
                // for(Map.Entry<String, Object> tmp: map.entrySet())
                // {
                //     res.put(tmp.getKey(), tmp.getValue().toString());
                // }
                // return res;
            }
        } catch (Exception e) {
        }
    }
}