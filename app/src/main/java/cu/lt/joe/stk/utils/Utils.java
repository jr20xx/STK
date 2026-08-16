package cu.lt.joe.stk.utils;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import cu.lt.joe.stk.accessibility.USSDAccessibilityService;
import cu.lt.joe.stk.fragments.dialog_fragments.PermissionRequesterDialogFragment;

public class Utils
{
    public static int dpToPx(Context context, int dp)
    {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void performCallFromFragment(@NonNull Fragment fragment, @NonNull Uri uriToDial)
    {
        if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            fragment.requireActivity().startActivity(new Intent(Intent.ACTION_CALL, uriToDial));
        else
            PermissionRequesterDialogFragment.newInstance(uriToDial).show(fragment.getChildFragmentManager(), null);
    }

    public static void copyToClipboard(@NonNull Context context, @Nullable String title, @NonNull String description)
    {
        try
        {
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText(title, description));
        }
        catch (Exception ignored)
        {
        }
    }

    public static String extractTextFromAccessibilityEvent(AccessibilityEvent event)
    {
        AccessibilityNodeInfo source = event.getSource();
        if (source != null)
        {
            StringBuilder textBuilder = new StringBuilder();
            appendTextFromAccessibilityNode(source, textBuilder);
            source.recycle();
            return textBuilder.toString();
        }
        return "";
    }

    private static void appendTextFromAccessibilityNode(AccessibilityNodeInfo node, StringBuilder textBuilder)
    {
        if (node != null)
        {
            if (node.getText() != null)
                textBuilder.append(node.getText().toString()).append(" ");
            for (int i = 0; i < node.getChildCount(); i++)
            {
                AccessibilityNodeInfo child = node.getChild(i);
                appendTextFromAccessibilityNode(child, textBuilder);
                if (child != null) child.recycle();
            }
        }
    }

    public static boolean isAccessibilityServiceEnabled(Context context)
    {
        for (AccessibilityServiceInfo serviceInfo : ((AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE)).getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC))
        {
            ServiceInfo currentServiceInfo = serviceInfo.getResolveInfo().serviceInfo;
            if (currentServiceInfo.packageName.equalsIgnoreCase(context.getPackageName()) && currentServiceInfo.name.equalsIgnoreCase(USSDAccessibilityService.class.getName()))
                return true;
        }
        return false;
    }

    public static void updateAppLocale(String languageTag)
    {
        if (languageTag == null)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        else
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag));
    }
}