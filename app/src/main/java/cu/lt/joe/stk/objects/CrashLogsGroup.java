package cu.lt.joe.stk.objects;

import java.util.ArrayList;

public class CrashLogsGroup
{
    private final String formattedDate;
    private final ArrayList<CrashLog> crashLogs;

    public CrashLogsGroup(String formattedDate, ArrayList<CrashLog> crashLogs)
    {
        this.formattedDate = formattedDate;
        this.crashLogs = crashLogs;
    }

    public String getFormattedDate()
    {
        return formattedDate;
    }

    public ArrayList<CrashLog> getCrashLogs()
    {
        return crashLogs;
    }
}