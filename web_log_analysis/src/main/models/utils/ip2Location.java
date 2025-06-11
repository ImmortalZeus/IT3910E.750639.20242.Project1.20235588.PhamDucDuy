package models.utils;

import java.io.IOException;
import java.util.HashMap;

import com.ip2location.*;

import models.exceptions.*;

public class ip2Location {
    // private static String IPv4DBPath = "./web_log_analysis/src/main/resources/ip2Location/IP2LOCATION-LITE-DB11.BIN";
    // private static String IPv6DBPath = "./web_log_analysis/src/main/resources/ip2Location/IP2LOCATION-LITE-DB11.IPV6.BIN";
    private static byte[] IPv4DBBytes = null;
    private static byte[] IPv6DBBytes = null;
    private static IP2Location locIPv4 = null;
    private static IP2Location locIPv6 = null;
    private static HashMap<String, IPResult> cache = new HashMap<String, IPResult>();
    private static IPTools iptools = new IPTools();

    public static final IPResult parse(String IP) throws ip2LocationException {
        if(IP == null) throw new ip2LocationException();
        try {
            if(cache.containsKey(IP)) {
                return cache.get(IP);
            } else {
                try {
                    if(iptools.IsIPv4(IP))
                    {
                        if(IPv4DBBytes == null)
                        {
                            try {
                                IPv4DBBytes = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/ip2Location/IP2LOCATION-LITE-DB11.BIN").readAllBytes();
                            } catch (IOException e) {
                                IPv4DBBytes = null;
                            }
                        }
                        if(IPv4DBBytes == null)
                        {
                            throw new ip2LocationException();
                        }
                        if(locIPv4 == null)
                        {
                            locIPv4 = new IP2Location();
                            locIPv4.Open(IPv4DBBytes);
                        }
                        IPResult rec = locIPv4.IPQuery(IP);
                        if("OK".equals(rec.getStatus()))
                        {
                            cache.put(IP, rec);
                            return rec;
                        }
                        else
                        {
                            throw new ip2LocationException();
                        }
                    } else if (iptools.IsIPv6(IP)) {
                        if(IPv6DBBytes == null)
                        {
                            try {
                                IPv6DBBytes = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/ip2Location/IP2LOCATION-LITE-DB11.IPV6.BIN").readAllBytes();
                            } catch (IOException e) {
                                IPv6DBBytes = null;
                            }
                        }
                        if(IPv6DBBytes == null)
                        {
                            throw new ip2LocationException();
                        }
                        if(locIPv6 == null)
                        {
                            locIPv6 = new IP2Location();
                            locIPv6.Open(IPv6DBBytes);
                        }
                        IPResult rec = locIPv6.IPQuery(IP);
                        if("OK".equals(rec.getStatus()))
                        {
                            cache.put(IP, rec);
                            return rec;
                        }
                        else
                        {
                            throw new ip2LocationException();
                        }
                    } else {
                        throw new ip2LocationException();
                    }
                } catch (Exception e) {
                    throw new ip2LocationException();
                }
            }
        } catch (Exception e) {
            throw new ip2LocationException();
        }
    }
}
