package cu.lt.joe.stk.objects;

public class CrashLog
{
    private final long ID;
    private final String title, body, formattedTimestamp;
    private boolean isSelected;

    public CrashLog(long ID, String title, String body, String formattedTimestamp)
    {
        this.ID = ID;
        this.title = title;
        this.body = body;
        this.formattedTimestamp = formattedTimestamp;
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

    public String getFormattedTimestamp()
    {
        return formattedTimestamp;
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