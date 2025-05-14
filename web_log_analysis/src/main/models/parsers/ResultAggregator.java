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
    private final Map<String, AtomicLong> countByByRequestUrl = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByHttpVer = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByCountryShort = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByCountryLong = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByRegion = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByCity = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByBrowser = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByOS = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> countByDevice = new ConcurrentHashMap<>();

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
        countByByRequestUrl
            .computeIfAbsent(e.getRequestUrl(), k -> new AtomicLong())
            .incrementAndGet();
        countByHttpVer
            .computeIfAbsent(e.getHttpVer(), k -> new AtomicLong())
            .incrementAndGet();
        countByCountryShort
            .computeIfAbsent(e.getCountryShort(), k -> new AtomicLong())
            .incrementAndGet();
        countByCountryLong
            .computeIfAbsent(e.getCountryLong(), k -> new AtomicLong())
            .incrementAndGet();
        countByRegion
            .computeIfAbsent(e.getRegion(), k -> new AtomicLong())
            .incrementAndGet();
        countByCity
            .computeIfAbsent(e.getCity(), k -> new AtomicLong())
            .incrementAndGet();
        countByBrowser
            .computeIfAbsent(e.getBrowser(), k -> new AtomicLong())
            .incrementAndGet();
        countByOS
            .computeIfAbsent(e.getOS(), k -> new AtomicLong())
            .incrementAndGet();
        countByDevice
            .computeIfAbsent(e.getDevice(), k -> new AtomicLong())
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

    public Map<String, AtomicLong> getCountByByRequestUrl() {
        return countByByRequestUrl;
    }

    public Map<String, AtomicLong> getCountByHttpVer() {
        return countByHttpVer;
    }

    public Map<String, AtomicLong> getCountByCountryShort() {
        return countByCountryShort;
    }

    public Map<String, AtomicLong> getCountByCountryLong() {
        return countByCountryLong;
    }

    public Map<String, AtomicLong> getCountByRegion() {
        return countByRegion;
    }

    public Map<String, AtomicLong> getCountByCity() {
        return countByCity;
    }

    public Map<String, AtomicLong> getCountByBrowser() {
        return countByBrowser;
    }

    public Map<String, AtomicLong> getCountByOS() {
        return countByOS;
    }

    public Map<String, AtomicLong> getCountByDevice() {
        return countByDevice;
    }

}
