package lineParsers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.*;

public class nginxLineParser {
    public HashMap<String, String> parse(String line) throws lineParserException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(line, new TypeReference<>() {});

            HashMap<String, String> res = new HashMap<String, String>();
            for(Map.Entry<String, Object> tmp: map.entrySet())
            {
                res.put(tmp.getKey(), tmp.getValue().toString());
            }
            return res;
        } catch (JsonProcessingException e) {
            throw new lineParserException();
        }
    }
}