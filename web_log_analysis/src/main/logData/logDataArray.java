package logData;

import java.util.Date;
import java.util.ArrayList;

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
    public ArrayList<logData> getByUser(String username) {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            String objUser = obj.getRemoteUser();
            if (objUser != null &&
                objUser.equals(username)) {
                    res.add(obj);
            }
        }
        return res;
    }
    public ArrayList<logData> getByRemoteIp(String remoteIp) {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            String objRemoteIp = obj.getRemoteIp();
            if (objRemoteIp != null &&
                objRemoteIp.equals(remoteIp)) {
                    res.add(obj);
            }
        }
        return res;
    }
    public ArrayList<logData> getByPeriod(Date startDate, Date endDate) {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            Date objTime = obj.getTime();
            if (objTime != null &&
                (objTime.equals(startDate) || objTime.after(startDate)) &&
                (objTime.equals(endDate) || objTime.after(endDate))) {
                    res.add(obj);
            }
        }
        return res;
    }
    public ArrayList<logData> getByRequestMethod(String RequestMethod) {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            String objRequestMethod = obj.getRequestMethod();
            if (objRequestMethod != null &&
                objRequestMethod.equals(RequestMethod)) {
                    res.add(obj);
            }
        }
        return res;
    }
    public ArrayList<logData> getByRequestURL(String RequestURL) {
        ArrayList<logData> res = new ArrayList<logData>();
        for (logData obj : this.data) {
            String objRequestURL = obj.getRequestURL();
            if (objRequestURL != null &&
                objRequestURL.equals(RequestURL)) {
                    res.add(obj);
            }
        }
        return res;
    }
}
