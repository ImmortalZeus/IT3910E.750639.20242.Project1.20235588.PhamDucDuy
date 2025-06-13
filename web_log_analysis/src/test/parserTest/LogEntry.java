package parserTest;
import java.util.Date;

public class LogEntry {
    private final String ip;
    private final String user;
    private final Date timestamp;
    private final String request;
    private final int status;
    private final long bytes;

    public LogEntry(String ip, String user, Date timestamp,
                    String request, int status, long bytes) {
        this.ip = ip;
        this.user = user;
        this.timestamp = timestamp;
        this.request = request;
        this.status = status;
        this.bytes = bytes;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getRequest() {
        return request;
    }

    public int getStatus() {
        return status;
    }

    public long getBytes() {
        return bytes;
    }

}
