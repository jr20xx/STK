package cu.lt.joe.stk.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.utils.Utils;

@SuppressLint("AccessibilityPolicy")
public class USSDAccessibilityService extends AccessibilityService
{
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        SharedPreferences sharp = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharp.edit();

        if (event.getClassName() != null && AlertDialog.class.getName().contentEquals(event.getClassName()))
        {
            String responseMessage = Utils.extractTextFromAccessibilityEvent(event).toUpperCase();
            Pattern pattern;
            Matcher matcher;
            if (responseMessage.contains("SALDO:"))
            {
                pattern = Pattern.compile("SALDO:\\s*([\\d.]+) (CUP)\\.");
                matcher = pattern.matcher(responseMessage);
                if (matcher.find())
                    spEditor.putString(Constants.LAST_KNOWN_BALANCE, matcher.group(1)).apply();

                pattern = Pattern.compile("DATOS:\\s*(\\d+)\\s*([KMGT]?B).");
                matcher = pattern.matcher(responseMessage);
                if (matcher.find())
                    spEditor.putString(Constants.LAST_KNOWN_INTERNET_DATA, matcher.group(1)).apply();

                pattern = Pattern.compile("SMS: (\\d+)\\.");
                matcher = pattern.matcher(responseMessage);
                if (matcher.find())
                    spEditor.putString(Constants.LAST_KNOWN_MESSAGES_COUNT, matcher.group(1)).apply();

                pattern = Pattern.compile("VOZ: (\\d+)\\.");
                matcher = pattern.matcher(responseMessage);
                if (matcher.find())
                    spEditor.putString(Constants.LAST_KNOWN_MINUTES_COUNT, matcher.group(1)).apply();
            }
            else if (responseMessage.contains("TARIFA:"))
            {
                pattern = Pattern.compile("DATOS:\\s*(\\d+)\\s*([KMGT]?B).");
                matcher = pattern.matcher(responseMessage);
                if (matcher.find())
                    spEditor.putString(Constants.LAST_KNOWN_INTERNET_DATA, matcher.group(1)).apply();
            }
            else if (responseMessage.contains("SMS"))
            {
                //TODO: Write code to handle SMS report
            }
            else if (responseMessage.contains("MIN"))
            {
                //TODO: Write code to handle minutes report
            }
            else if (responseMessage.contains("RECARGA"))
            {
                //TODO: Write code to handle recharge report
            }
            else
            {
                //TODO: Write code to handle bonuses report, since is the only thing left to possibly handle
            }
        }
    }

    @Override
    public void onInterrupt()
    {
    }

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.android.phone"};
        serviceInfo.flags = AccessibilityServiceInfo.DEFAULT;
        setServiceInfo(serviceInfo);
    }
}