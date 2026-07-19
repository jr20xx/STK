package cu.lt.joe.stk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.activities.ShortcutActivity;

public class ShortcutUtils
{
    public static Bitmap getShortcutIconBitmap(Context context, int backgroundColor, int foregroundResId)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int iconSize = am.getLauncherLargeIconSize();

        Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroundColor);

        Drawable vector = ContextCompat.getDrawable(context, foregroundResId);
        vector.setTint(Color.WHITE);
        int padding = (int) (iconSize * 0.25f); // 25% padding
        int drawableSize = iconSize - 2 * padding;
        vector.setBounds(padding, padding, padding + drawableSize, padding + drawableSize);
        vector.draw(canvas);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            bitmap = roundBitmap(context, bitmap);
        return bitmap;
    }

    public static Intent getTargetIntent(Context context, String dialingNumber)
    {
        return new Intent(context, ShortcutActivity.class).setAction(Constants.SHORTCUT_ACTION_DIAL)
                .putExtra(Constants.SHORTCUT_USSD_CODE, dialingNumber).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private static Bitmap roundBitmap(Context context, Bitmap src)
    {
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(context.getResources(), src);
        rounded.setCornerRadius(src.getWidth() * 0.2f);
        rounded.setAntiAlias(true);

        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        rounded.setBounds(0, 0, src.getWidth(), src.getHeight());
        rounded.draw(canvas);
        canvas.setBitmap(null);

        return result;
    }
}