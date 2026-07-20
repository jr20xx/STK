package cu.lt.joe.stk.objects;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class ShortcutItem
{
    private final int iconResId;
    private final String shortTitle, longTitle, dialingNumber;

    public ShortcutItem(int iconResId, String shortTitle, String longTitle, String dialingNumber)
    {
        this.iconResId = iconResId;
        this.shortTitle = shortTitle;
        this.dialingNumber = dialingNumber;
        this.longTitle = longTitle;
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

    public String getShortTitle()
    {
        return shortTitle;
    }

    public String getLongTitle()
    {
        return longTitle;
    }

    public String getDialingNumber()
    {
        return dialingNumber;
    }
}