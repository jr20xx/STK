package cu.lt.joe.stk.objects;

import android.content.Intent;

public class AdditionalOptionsMenuItem extends BaseItem
{
    private final int iconResId;
    private final String title;
    private final Intent targetIntent;

    public AdditionalOptionsMenuItem(int iconResId, String title, Intent targetIntent)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.targetIntent = targetIntent;
    }

    public int getIconResId()
    {
        return iconResId;
    }

    public String getTitle()
    {
        return title;
    }

    public Intent getTargetIntent()
    {
        return targetIntent;
    }
}