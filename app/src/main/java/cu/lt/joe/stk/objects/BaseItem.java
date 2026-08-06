package cu.lt.joe.stk.objects;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class BaseItem
{
    @BindingAdapter("android:drawable")
    public static void setIcon(@NonNull ImageView iconImageView, int iconResId)
    {
        iconImageView.setImageResource(iconResId);
    }
}