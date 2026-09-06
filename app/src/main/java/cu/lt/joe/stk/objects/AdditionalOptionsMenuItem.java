package cu.lt.joe.stk.objects;

import android.content.Intent;
import android.view.View;

public class AdditionalOptionsMenuItem extends BaseItem
{
    private final int iconResId;
    private final String title;
    private Intent targetIntent;
    private View.OnClickListener onClickListener;

    public AdditionalOptionsMenuItem(int iconResId, String title, Intent targetIntent)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.targetIntent = targetIntent;
    }

    public AdditionalOptionsMenuItem(int iconResId, String title, View.OnClickListener onClickListener)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.onClickListener = onClickListener;
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

    public View.OnClickListener getOnClickListener()
    {
        return onClickListener;
    }
}