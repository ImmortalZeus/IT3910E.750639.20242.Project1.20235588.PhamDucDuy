package models.logData;

import java.util.Date;
import java.util.HashMap;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class logData {
    @BsonProperty("index")
    private Integer index = null;
    @BsonProperty("time")
    private Date time = null;
    @BsonProperty("remoteIp")
    private String remoteIp = null;
    @BsonProperty("remoteUser")
    private String remoteUser = null;
    @BsonProperty("request")
    private String request = null;
    @BsonProperty("responseStatusCode")
    private Integer responseStatusCode = null;
    @BsonProperty("bytes")
    private Integer bytes = null;
    @BsonProperty("referrer")
    private String referrer = null;
    @BsonProperty("agent")
    private String agent = null;
    @BsonProperty("requestMethod")
    private String requestMethod = null;
    @BsonProperty("requestUrl")
    private String requestUrl = null;
    @BsonProperty("httpVer")
    private String httpVer = null;
    @BsonProperty("countryShort")
    private String countryShort = null;
    @BsonProperty("countryLong")
    private String countryLong = null;
    @BsonProperty("region")
    private String region = null;
    @BsonProperty("city")
    private String city = null;
    @BsonProperty("zipCode")
    private String zipCode = null;
    @BsonProperty("timeZone")
    private String timeZone = null;
    @BsonProperty("browser")
    private String browser = null;
    @BsonProperty("os")
    private String OS = null;
    @BsonProperty("device")
    private String device = null;
    public logData() {}
    public logData(HashMap<String, Object> data) {
        Object tmpindex = data.get("index");
        if(tmpindex instanceof Integer) {
            this.index = (Integer) tmpindex;
        } else {
            this.index = null;
        }
        Object tmptime = data.get("time");
        if(tmptime instanceof Date) {
            this.time = new Date(((Date) tmptime).getTime());
        } else {
            this.time = null;
        }
        Object tmpremoteIp = data.get("remote_ip");
        if(tmpremoteIp instanceof String) {
            this.remoteIp = (String) tmpremoteIp;
        } else {
            this.remoteIp = null;
        }
        Object tmpremoteUser = data.get("remote_user");
        if(tmpremoteUser instanceof String) {
            this.remoteUser = (String) tmpremoteUser;
        } else {
            this.remoteUser = null;
        }
        Object tmprequest = data.get("request");
        if(tmprequest instanceof String) {
            this.request = (String) tmprequest;
        } else {
            this.request = null;
        }
        Object tmpresponseStatusCode = data.get("response_status_code");
        if(tmpresponseStatusCode instanceof Integer) {
            this.responseStatusCode = (Integer) tmpresponseStatusCode;
        } else {
            this.responseStatusCode = null;
        }
        Object tmpbytes = data.get("bytes");
        if(tmpbytes instanceof Integer) {
            this.bytes = (Integer) tmpbytes;
        } else {
            this.bytes = null;
        }
        Object tmpreferrer = data.get("referrer");
        if(tmpreferrer instanceof String) {
            this.referrer = (String) tmpreferrer;
        } else {
            this.referrer = null;
        }
        Object tmpagent = data.get("agent");
        if(tmpagent instanceof String) {
            this.agent = (String) tmpagent;
        } else {
            this.agent = null;
        }
        Object tmprequestMethod = data.get("request_method");
        if(tmprequestMethod instanceof String) {
            this.requestMethod = (String) tmprequestMethod;
        } else {
            this.requestMethod = null;
        }
        Object tmprequestUrl = data.get("request_url");
        if(tmprequestUrl instanceof String) {
            this.requestUrl = (String) tmprequestUrl;
        } else {
            this.requestUrl = null;
        }
        Object tmphttpVer = data.get("http_ver");
        if(tmphttpVer instanceof String) {
            this.httpVer = (String) tmphttpVer;
        } else {
            this.httpVer = null;
        }
        Object tmpcountryShort = data.get("country_short");
        if(tmpcountryShort instanceof String) {
            this.countryShort = (String) tmpcountryShort;
        } else {
            this.countryShort = null;
        }
        Object tmpcountryLong = data.get("country_long");
        if(tmpcountryLong instanceof String) {
            this.countryLong = (String) tmpcountryLong;
        } else {
            this.countryLong = null;
        }
        Object tmpregion = data.get("region");
        if(tmpregion instanceof String) {
            this.region = (String) tmpregion;
        } else {
            this.region = null;
        }
        Object tmpcity = data.get("city");
        if(tmpcity instanceof String) {
            this.city = (String) tmpcity;
        } else {
            this.city = null;
        }
        Object tmpzipCode = data.get("zip_code");
        if(tmpzipCode instanceof String) {
            this.zipCode = (String) tmpzipCode;
        } else {
            this.zipCode = null;
        }
        Object tmptimeZone = data.get("time_zone");
        if(tmptimeZone instanceof String) {
            this.timeZone = (String) tmptimeZone;
        } else {
            this.timeZone = null;
        }
        Object tmpbrowser = data.get("browser");
        if(tmpbrowser instanceof String) {
            this.browser = (String) tmpbrowser;
        } else {
            this.browser = null;
        }
        Object tmpOS = data.get("OS");
        if(tmpOS instanceof String) {
            this.OS = (String) tmpOS;
        } else {
            this.OS = null;
        }
        Object tmpdevice = data.get("device");
        if(tmpdevice instanceof String) {
            this.device = (String) tmpdevice;
        } else {
            this.device = null;
        }
    }

    public logData(Integer index, Date time, String remoteIp, String remoteUser, String request, Integer responseStatusCode,
            Integer bytes, String referrer, String agent, String requestMethod, String requestUrl, String httpVer,
            String countryShort, String countryLong, String region, String city,
            String zipCode, String timeZone, String browser, String OS, String device) {
        this.index = index;
        this.time = time == null ? time : new Date(time.getTime());
        this.remoteIp = remoteIp;
        this.remoteUser = remoteUser;
        this.request = request;
        this.responseStatusCode = responseStatusCode;
        this.bytes = bytes;
        this.referrer = referrer;
        this.agent = agent;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.httpVer = httpVer;
        this.countryShort = countryShort;
        this.countryLong = countryLong;
        this.region = region;
        this.city = city;
        this.zipCode = zipCode;
        this.timeZone = timeZone;
        this.browser = browser;
        this.OS = OS;
        this.device = device;
    }
    public Integer getIndex() {
        return this.index;
    }
    public Date getTime() {
        return this.time == null ? this.time : new Date(this.time.getTime());
    }
    public String getRemoteIp() {
        return this.remoteIp;
    }
    public String getRemoteUser() {
        return this.remoteUser;
    }
    public String getRequest() {
        return this.request;
    }
    public Integer getResponseStatusCode() {
        return this.responseStatusCode;
    }
    public Integer getBytes() {
        return this.bytes;
    }
    public String getReferrer() {
        return this.referrer;
    }
    public String getAgent() {
        return this.agent;
    }
    public String getRequestMethod() {
        return this.requestMethod;
    }
    public String getRequestUrl() {
        return this.requestUrl;
    }
    public String getHttpVer() {
        return this.httpVer;
    }
    public String getCountryShort() {
        return this.countryShort;
    }
    public String getCountryLong() {
        return this.countryLong;
    }
    public String getRegion() {
        return this.region;
    }
    public String getCity() {
        return this.city;
    }
    public String getZipCode() {
        return this.zipCode;
    }
    public String getTimeZone() {
        return this.timeZone;
    }
    public String getBrowser() {
        return browser;
    }
    public String getOS() {
        return OS;
    }
    public String getDevice() {
        return device;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
    public void setTime(Date time) {
        this.time = time == null ? time : new Date(time.getTime());
    }
    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }
    public void setRequest(String request) {
        this.request = request;
    }
    public void setResponseStatusCode(Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }
    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
    public void setAgent(String agent) {
        this.agent = agent;
    }
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    public void setHttpVer(String httpVer) {
        this.httpVer = httpVer;
    }
    public void setCountryShort(String countryShort) {
        this.countryShort = countryShort;
    }
    public void setCountryLong(String countryLong) {
        this.countryLong = countryLong;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public void setOS(String OS) {
        this.OS = OS;
    }
    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "Time" + " : " + this.getTime() + '\n' +
                "Remote Ip" + " : " + this.getRemoteIp() + '\n' +
                "Remote User" + " : " + this.getRemoteUser() + '\n' + 
                "Request" + " : " + this.getRequest() + '\n' + 
                "Response Status Code" + " : " + this.getResponseStatusCode() + '\n' + 
                "Bytes" + " : " + this.getBytes() + '\n' + 
                "Referrer" + " : " + this.getReferrer() + '\n' + 
                "Agent" + " : " + this.getAgent() + '\n' + 
                "Request Method" + " : " + this.getRequestMethod() + '\n' + 
                "Request URL" + " : " + this.getRequestUrl() + '\n' + 
                "Http Ver" + " : " + this.getHttpVer() + '\n' +
                "Country Short" + " : " + this.getCountryShort() + '\n' +
                "Country Long" + " : " + this.getCountryLong() + '\n' +
                "Region" + " : " + this.getRegion() + '\n' + 
                "City" + " : " + this.getCity() + '\n' + 
                "Zip Code" + " : " + this.getZipCode() + '\n' + 
                "Time Zone" + " : " + this.getTimeZone() + '\n' + 
                "Browser" + " : " + this.getBrowser() + '\n' + 
                "OS" + " : " + this.getOS() + '\n' + 
                "Device" + " : " + this.getDevice();
    }
}
