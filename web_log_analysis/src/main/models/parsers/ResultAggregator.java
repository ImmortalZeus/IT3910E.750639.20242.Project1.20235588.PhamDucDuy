package models.parsers;

import java.util.concurrent.atomic.*;

import models.logData.logData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultAggregator {
    private final AtomicLong count = new AtomicLong();
    private final Map<String, AtomicLong> countByRemoteIp = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByRemoteUser = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByRequest = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicLong> countByResponseStatusCode = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByReferrer = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByAgent = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByRequestMethod = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByRequestURL = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByHttpVer = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByCountryShort = new ConcurrentHashMap<>();

    public void collect(logData e) {
        count.incrementAndGet();
        countByRemoteIp
            .computeIfAbsent(e.getRemoteIp(), k -> new AtomicLong())
            .incrementAndGet();
        countByRemoteUser
            .computeIfAbsent(e.getRemoteUser(), k -> new AtomicLong())
            .incrementAndGet();
        countByRequest
            .computeIfAbsent(e.getRequest(), k -> new AtomicLong())
            .incrementAndGet();
        countByResponseStatusCode
            .computeIfAbsent(e.getResponseStatusCode(), k -> new AtomicLong())
            .incrementAndGet();
        countByReferrer
            .computeIfAbsent(e.getReferrer(), k -> new AtomicLong())
            .incrementAndGet();
        countByAgent
            .computeIfAbsent(e.getAgent(), k -> new AtomicLong())
            .incrementAndGet();
        countByRequestMethod
            .computeIfAbsent(e.getRequestMethod(), k -> new AtomicLong())
            .incrementAndGet();
        countByRequestURL
            .computeIfAbsent(e.getRequestURL(), k -> new AtomicLong())
            .incrementAndGet();
        countByHttpVer
            .computeIfAbsent(e.getHttpVer(), k -> new AtomicLong())
            .incrementAndGet();
        countByCountryShort
            .computeIfAbsent(e.getGeoInfo().getCountryShort(), k -> new AtomicLong())
            .incrementAndGet();
    }

    public ResultAggregator report() {
        return this;
    }

    public AtomicLong getCount() {
        return count;
    }

    public Map<String, AtomicLong> getCountByRemoteIp() {
        return countByRemoteIp;
    }

    public Map<String, AtomicLong> getCountByRemoteUser() {
        return countByRemoteUser;
    }

    public Map<String, AtomicLong> getCountByRequest() {
        return countByRequest;
    }

    public Map<Integer, AtomicLong> getCountByResponseStatusCode() {
        return countByResponseStatusCode;
    }

    public Map<String, AtomicLong> getCountByReferrer() {
        return countByReferrer;
    }

    public Map<String, AtomicLong> getCountByAgent() {
        return countByAgent;
    }

    public Map<String, AtomicLong> getCountByRequestMethod() {
        return countByRequestMethod;
    }

    public Map<String, AtomicLong> getCountByRequestURL() {
        return countByRequestURL;
    }

    public Map<String, AtomicLong> getCountByHttpVer() {
        return countByHttpVer;
    }

    public Map<String, AtomicLong> getCountByCountryShort() {
        return countByCountryShort;
    }
}
