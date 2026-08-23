package cu.lt.joe.stk.objects;

public class CrashLog
{
    private final String title, body;
    private final long ID, timestamp;
    private boolean isSelected;

    public CrashLog(long ID, String title, String body, long timestamp)
    {
        this.ID = ID;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public long getID()
    {
        return ID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }
}