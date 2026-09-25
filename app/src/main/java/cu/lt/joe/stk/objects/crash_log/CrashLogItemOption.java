package cu.lt.joe.stk.objects.crash_log;

import android.view.View;
import cu.lt.joe.stk.objects.BaseItem;

public class CrashLogItemOption extends BaseItem
{
    private final int iconResId;
    private final String label;
    private final View.OnClickListener onClickListener;

    public CrashLogItemOption(int iconResId, String label, View.OnClickListener onClickListener)
    {
        this.iconResId = iconResId;
        this.label = label;
        this.onClickListener = onClickListener;
    }

    public int getIconResId()
    {
        return iconResId;
    }

    public String getLabel()
    {
        return label;
    }

    public View.OnClickListener getOnClickListener()
    {
        return onClickListener;
    }
}