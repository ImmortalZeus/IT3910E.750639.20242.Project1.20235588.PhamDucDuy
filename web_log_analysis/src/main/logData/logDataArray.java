package logData;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class logDataArray {
    private ArrayList<logData> data = new ArrayList<logData>();

    public logDataArray(ArrayList<logData> data) {
        this.data = data;
    }
    public ArrayList<logData> getAllLogData() {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            res.add(obj);
        }
        return res;
    }
    /*
    public logDataArray getByRemoteUser(String remoteUser) {
        HashMap<String, Object> filter_rules = new HashMap<String, Object>();
        filter_rules.put("byRemoteUser", true);
        filter_rules.put("byRemoteUserValue", remoteUser);
        return this.filter(filter_rules);
    }
    public logDataArray getByRemoteIp(String remoteIp) {
        HashMap<String, Object> filter_rules = new HashMap<String, Object>();
        filter_rules.put("byRemoteIp", true);
        filter_rules.put("byRemoteIpValue", remoteIp);
        return this.filter(filter_rules);
    }
    public logDataArray getByPeriod(Date startDate, Date endDate) {
        HashMap<String, Object> filter_rules = new HashMap<String, Object>();
        filter_rules.put("byPeriod", true);
        filter_rules.put("byPeriodStartValue", startDate);
        filter_rules.put("byPeriodEndValue", endDate);
        return this.filter(filter_rules);
    }
    public logDataArray getByRequestMethod(String RequestMethod) {
        HashMap<String, Object> filter_rules = new HashMap<String, Object>();
        filter_rules.put("byRequestMethod", true);
        filter_rules.put("byRequestMethodValue", RequestMethod);
        return this.filter(filter_rules);
    }
    public logDataArray getByRequestURL(String RequestURL) {
        HashMap<String, Object> filter_rules = new HashMap<String, Object>();
        filter_rules.put("byRequestURL", true);
        filter_rules.put("byRequestURLValue", RequestURL);
        return this.filter(filter_rules);
    }
    */
    public logDataArray filter(HashMap<String, Object> filter_rules) {
        /*
        filter_rules {
            "byPeriod": Boolean (true | false),
            "byPeriodStartValue": Date,
            "byPeriodEndValue": Date,

            "byRemoteUser": Boolean (true | false),
            "byRemoteUserValue": String,

            "byRemoteIp": Boolean (true | false),
            "byRemoteIpValue": String,

            "byRequest": Boolean (true | false),
            "byRequestValue": String,

            "byResponseStatusCode": Boolean (true | false),
            "byResponseStatusCodeValue": Integer,

            "byBytes": Boolean (true | false),
            "byBytesValue": Integer,

            "byReferrer": Boolean (true | false),
            "byReferrerValue": String,

            "byAgent": Boolean (true | false),
            "byAgentValue": String,

            "byRequestMethod": Boolean (true | false),
            "byRequestMethodValue": String,

            "byRequestURL": Boolean (true | false),
            "byRequestURLValue": String,

            "byHttpVer": Boolean (true | false),
            "byHttpVerValue": String,
        }
        */
        Boolean filter_rules_byPeriod = (Boolean) filter_rules.get("byPeriod");
        Date filter_rules_byPeriodStartValue = (Date) filter_rules.get("byPeriodStartValue");
        Date filter_rules_byPeriodEndValue = (Date) filter_rules.get("byPeriodEndValue");

        Boolean filter_rules_byRemoteIp = (Boolean) filter_rules.get("byRemoteIp");
        String filter_rules_byRemoteIpValue = (String) filter_rules.get("byRemoteIpValue");

        Boolean filter_rules_byRemoteUser = (Boolean) filter_rules.get("byRemoteUser");
        String filter_rules_byRemoteUserValue = (String) filter_rules.get("byRemoteUserValue");

        Boolean filter_rules_byRequest = (Boolean) filter_rules.get("byRequest");
        String filter_rules_byRequestValue = (String) filter_rules.get("byRequestValue");

        Boolean filter_rules_byResponseStatusCode = (Boolean) filter_rules.get("byResponseStatusCode");
        Integer filter_rules_byResponseStatusCodeValue = (Integer) filter_rules.get("byResponseStatusCodeValue");

        Boolean filter_rules_byBytes = (Boolean) filter_rules.get("byBytes");
        Integer filter_rules_byBytesValue = (Integer) filter_rules.get("byBytesValue");

        Boolean filter_rules_byReferrer = (Boolean) filter_rules.get("byReferrer");
        String filter_rules_byReferrerValue = (String) filter_rules.get("byReferrerValue");

        Boolean filter_rules_byAgent = (Boolean) filter_rules.get("byAgent");
        String filter_rules_byAgentValue = (String) filter_rules.get("byAgentValue");

        Boolean filter_rules_byRequestMethod = (Boolean) filter_rules.get("byRequestMethod");
        String filter_rules_byRequestMethodValue = (String) filter_rules.get("byRequestMethodValue");

        Boolean filter_rules_byRequestURL = (Boolean) filter_rules.get("byRequestURL");
        String filter_rules_byRequestURLValue = (String) filter_rules.get("byRequestURLValue");

        Boolean filter_rules_byHttpVer = (Boolean) filter_rules.get("byHttpVer");
        String filter_rules_byHttpVerValue = (String) filter_rules.get("byHttpVerValue");
        
        ArrayList<logData> res = new ArrayList<logData>();

        for (logData obj : this.data) {
            Date objTime = obj.getTime();
            String objRemoteIp = obj.getRemoteIp();
            String objRemoteUser = obj.getRemoteUser();
            String objRequest = obj.getRequest();
            Integer objResponseStatusCode = obj.getResponseStatusCode();
            Integer objBytes = obj.getBytes();
            String objReferrer = obj.getReferrer();
            String objAgent = obj.getAgent();
            String objRequestMethod = obj.getRequestMethod();
            String objRequestURL = obj.getRequestURL();
            String objHttpVer = obj.getHttpVer();

            if (
                (filter_rules_byPeriod == null || filter_rules_byPeriod.equals(false) || (objTime != null && (objTime.equals(filter_rules_byPeriodStartValue) || objTime.after(filter_rules_byPeriodStartValue)) && (objTime.equals(filter_rules_byPeriodEndValue) || objTime.after(filter_rules_byPeriodEndValue)))) && 
                (filter_rules_byRemoteIp == null || filter_rules_byRemoteIp.equals(false) || (objRemoteIp != null && objRemoteIp.equals(filter_rules_byRemoteIpValue))) && 
                (filter_rules_byRemoteUser == null || filter_rules_byRemoteUser.equals(false) || (objRemoteUser != null && objRemoteUser.equals(filter_rules_byRemoteUserValue))) && 
                (filter_rules_byRequest == null || filter_rules_byRequest.equals(false) || (objRequest != null && objRequest.equals(filter_rules_byRequestValue))) && 
                (filter_rules_byResponseStatusCode == null || filter_rules_byResponseStatusCode.equals(false) || (objResponseStatusCode != null && objResponseStatusCode.equals(filter_rules_byResponseStatusCodeValue))) && 
                (filter_rules_byBytes == null || filter_rules_byBytes.equals(false) || (objBytes != null && objBytes.equals(filter_rules_byBytesValue))) && 
                (filter_rules_byReferrer == null || filter_rules_byReferrer.equals(false) || (objReferrer != null && objReferrer.equals(filter_rules_byReferrerValue))) && 
                (filter_rules_byAgent == null || filter_rules_byAgent.equals(false) || (objAgent != null && objAgent.equals(filter_rules_byAgentValue))) && 
                (filter_rules_byRequestMethod == null || filter_rules_byRequestMethod.equals(false) || (objRequestMethod != null && objRequestMethod.equals(filter_rules_byRequestMethodValue))) && 
                (filter_rules_byRequestURL == null || filter_rules_byRequestURL.equals(false) || (objRequestURL != null && objRequestURL.equals(filter_rules_byRequestURLValue))) && 
                (filter_rules_byHttpVer == null || filter_rules_byHttpVer.equals(false) || (objHttpVer != null && objHttpVer.equals(filter_rules_byHttpVerValue)))
                ) {
                    res.add(obj);
            }
        }
        return (new logDataArray(res));
    }
}
