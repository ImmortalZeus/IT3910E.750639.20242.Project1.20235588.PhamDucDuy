package models.utils;

import java.util.HashMap;

import ua_parser.Parser;
import ua_parser.Client;

import models.exceptions.*;

public class userAgentParser {
    private static final Parser useragentParser = new Parser();
    private static HashMap<String, Client> cache = new HashMap<String, Client>();

    public static final Client parse(String ua) throws userAgentParserException {
        if(ua == null) throw new userAgentParserException();
        try {
            if(cache.containsKey(ua)) {
                return cache.get(ua);
            } else {
                Client useragentParsed = useragentParser.parse(ua);
                cache.put(ua, useragentParsed);
                return useragentParsed;
            }
        } catch (Exception e) {
            throw new userAgentParserException();            
        }
    }
}
