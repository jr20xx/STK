package cu.lt.joe.stk.objects.crash_log;

import cu.lt.joe.stk.objects.BaseItem;

public class CrashLogItemOption extends BaseItem
{
    private final int iconResId;
    private final String label;

    public CrashLogItemOption(int iconResId, String label)
    {
        this.iconResId = iconResId;
        this.label = label;
    }

    public int getIconResId()
    {
        return iconResId;
    }

    public String getLabel()
    {
        return label;
    }
}