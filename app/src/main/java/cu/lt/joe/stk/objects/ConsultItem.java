package cu.lt.joe.stk.objects;

import android.net.Uri;

public class ConsultItem extends BaseItem
{
    private final int iconResId;
    private final String title;
    private final Uri uriToDial;
    private String availableInfo;

    public ConsultItem(int iconResId, String title, String availableInfo, Uri uriToDial)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.availableInfo = availableInfo;
        this.uriToDial = uriToDial;
    }

    public int getIconResId()
    {
        return iconResId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAvailableInfo()
    {
        return availableInfo;
    }

    public void setAvailableInfo(String availableInfo)
    {
        this.availableInfo = availableInfo;
    }

    public Uri getUriToDial()
    {
        return uriToDial;
    }
}