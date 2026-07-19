package cu.lt.joe.stk.objects;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class ShortcutItem
{
    private final int iconResId;
    private final String title, dialingNumber;

    public ShortcutItem(int iconResId, String title, String dialingNumber)
    {
        this.iconResId = iconResId;
        this.title = title;
        this.dialingNumber = dialingNumber;
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

    public String getDialingNumber()
    {
        return dialingNumber;
    }
}