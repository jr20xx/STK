package cu.lt.joe.stk.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import cu.lt.joe.stk.fragments.dialog_fragments.PermissionRequesterFragment;

public class Utils
{
    public static int dpToPx(Context context, int dp)
    {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void performCall(Fragment fragment, Uri uriToDial)
    {
        if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            fragment.requireActivity().startActivity(new Intent(Intent.ACTION_CALL, uriToDial));
        else
            PermissionRequesterFragment.newInstance(uriToDial).show(fragment.getChildFragmentManager(), null);
    }
}