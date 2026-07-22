package cu.lt.joe.stk.objects;

import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class ConsultItem
{
    private final int iconResId;
    private final String title;
    private String availableInfo;
    private final Uri uriToDial;

    public ConsultItem(int iconResId, String title, String availableInfo, Uri uriToDial)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.availableInfo = availableInfo;
        this.uriToDial = uriToDial;
    }

    @BindingAdapter("android:drawable")
    public static void setIcon(@NonNull ImageView iconImageView, int iconResId)
    {
        iconImageView.setImageResource(iconResId);
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

    public Uri getUriToDial()
    {
        return uriToDial;
    }

    public void setAvailableInfo(String availableInfo)
    {
        this.availableInfo = availableInfo;
    }
}