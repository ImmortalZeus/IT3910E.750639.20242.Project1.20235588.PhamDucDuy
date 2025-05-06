package models.logData;

import java.util.Date;
import java.util.HashMap;

import models.geoInfo.geoInfo;

public class logData {
    private Date time;
    private String remoteip;
    private String remoteuser;
    private String request;
    private Integer responsestatuscode;
    private Integer bytes;
    private String referrer;
    private String agent;
    private String requestmethod;
    private String requesturl;
    private String httpver;
    private geoInfo geoinfo;

    public logData(HashMap<String, Object> data) {
        Object tmptime = data.get("time");
        if(tmptime instanceof Date) {
            this.time = (Date) tmptime;
        } else {
            this.time = null;
        }
        Object tmpremoteip = data.get("remote_ip");
        if(tmpremoteip instanceof String) {
            this.remoteip = (String) tmpremoteip;
        } else {
            this.remoteip = null;
        }
        Object tmpremoteuser = data.get("remote_user");
        if(tmpremoteuser instanceof String) {
            this.remoteuser = (String) tmpremoteuser;
        } else {
            this.remoteuser = null;
        }
        Object tmprequest = data.get("request");
        if(tmprequest instanceof String) {
            this.request = (String) tmprequest;
        } else {
            this.request = null;
        }
        Object tmpresponsestatuscode = data.get("response_status_code");
        if(tmpresponsestatuscode instanceof Integer) {
            this.responsestatuscode = (Integer) tmpresponsestatuscode;
        } else {
            this.responsestatuscode = null;
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
        Object tmprequestmethod = data.get("request_method");
        if(tmprequestmethod instanceof String) {
            this.requestmethod = (String) tmprequestmethod;
        } else {
            this.requestmethod = null;
        }
        Object tmprequesturl = data.get("request_url");
        if(tmprequesturl instanceof String) {
            this.requesturl = (String) tmprequesturl;
        } else {
            this.requesturl = null;
        }
        Object tmphttpver = data.get("http_ver");
        if(tmphttpver instanceof String) {
            this.httpver = (String) tmphttpver;
        } else {
            this.httpver = null;
        }
        Object tmpgeoinfo = data.get("geo_info");
        if(tmpgeoinfo instanceof geoInfo) {
            this.geoinfo = (geoInfo) tmpgeoinfo;
        } else {
            this.geoinfo = null;
        }
    }

    public Date getTime() {
        return this.time;
    }
    public String getRemoteIp() {
        return this.remoteip;
    }
    public String getRemoteUser() {
        return this.remoteuser;
    }
    public String getRequest() {
        return this.request;
    }
    public Integer getResponseStatusCode() {
        return this.responsestatuscode;
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
        return this.requestmethod;
    }
    public String getRequestURL() {
        return this.requesturl;
    }
    public String getHttpVer() {
        return this.httpver;
    }
    public geoInfo getGeoInfo() {
        return this.geoinfo;
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
                "Request URL" + " : " + this.getRequestURL() + '\n' + 
                "Http Ver" + " : " + this.getHttpVer() + '\n' +
                "Geo Info" + " : " + this.getGeoInfo();
    }
}
