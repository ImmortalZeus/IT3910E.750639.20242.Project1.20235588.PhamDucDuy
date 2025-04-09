package lineParsers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.*;
import logData.logData;
import utils.parsedValueClassConverter;

public class nginxLineParser {
    private static parsedValueClassConverter pVCC = new parsedValueClassConverter();
    public logData parse(String line) throws lineParserException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(line, new TypeReference<>() {});

            
            String request = (String) map.get("request");
            final String regex = "((?<RequestMethod>GET|POST|HEAD|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH)\\s(?<RequestURL>\\/[^\\s]*)\\s(?<HttpVer>HTTP/\\d\\.\\d))";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(request);

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

            map.put("request_method", parsedData.get("RequestMethod"));
            map.put("request_url", parsedData.get("RequestURL"));
            map.put("http_ver", parsedData.get("HttpVer"));
            
            map = pVCC.fix(map);
            
            logData res = new logData(map);
            return res;

            // HashMap<String, String> res = new HashMap<String, String>();
            // for(Map.Entry<String, Object> tmp: map.entrySet())
            // {
            //     res.put(tmp.getKey(), tmp.getValue().toString());
            // }
            // return res;
        } catch (JsonProcessingException e) {
            throw new lineParserException();
        }
    }
}