package logData;

import java.util.Date;
import java.util.HashMap;

public class logData {
    private HashMap<String, Object> data = new HashMap<String, Object>();

    public logData(HashMap<String, Object> data) {
        this.data = data;
    }

    public Date getTime() {
        Object tmp = this.data.get("time");
        if(tmp instanceof Date) {
            return (Date) tmp;
        } else {
            return null;
        }
    }
    public String getRemoteIp() {
        Object tmp = this.data.get("remote_ip");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getRemoteUser() {
        Object tmp = this.data.get("remote_user");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getRequest() {
        Object tmp = this.data.get("request");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public Integer getResponse() {
        Object tmp = this.data.get("response");
        if(tmp instanceof Integer) {
            return (Integer) tmp;
        } else {
            return null;
        }
    }
    public Integer getBytes() {
        Object tmp = this.data.get("bytes");
        if(tmp instanceof Integer) {
            return (Integer) tmp;
        } else {
            return null;
        }
    }
    public String getReferrer() {
        Object tmp = this.data.get("referrer");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getAgent() {
        Object tmp = this.data.get("agent");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getRequestMethod() {
        Object tmp = this.data.get("request_method");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getRequestURL() {
        Object tmp = this.data.get("request_url");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
    public String getHttpVer() {
        Object tmp = this.data.get("http_ver");
        if(tmp instanceof String) {
            return (String) tmp;
        } else {
            return null;
        }
    }
}
