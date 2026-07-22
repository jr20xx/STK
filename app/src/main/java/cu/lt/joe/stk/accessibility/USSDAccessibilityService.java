package cu.lt.joe.stk.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
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
            String wordsFromMessage[] = responseMessage.toUpperCase().split(" ");
            for (int i = 0; i < wordsFromMessage.length; i++)
            {
                String currentWord = wordsFromMessage[i];
                switch (currentWord)
                {
                    case "SALDO:":
                        spEditor.putString(Constants.LAST_KNOWN_BALANCE, wordsFromMessage[i + 1] + " " + wordsFromMessage[i + 2].replace(".", "")).apply();
                        i += 2;
                        break;
                    case "DATOS:":
                        spEditor.putString(Constants.LAST_KNOWN_INTERNET_DATA, wordsFromMessage[i + 1] + " " + wordsFromMessage[i + 2].replace(".", "")).apply();
                        i += 2;
                        break;
                    case "VOZ:":
                        spEditor.putString(Constants.LAST_KNOWN_MINUTES_COUNT, wordsFromMessage[i + 1].replace(".", "")).apply();
                        i++;
                        break;
                    case "SMS:":
                        spEditor.putString(Constants.LAST_KNOWN_MESSAGES_COUNT, wordsFromMessage[i + 1].replace(".", "")).apply();
                        i++;
                        break;
                    case "SMS":
                        spEditor.putString(Constants.LAST_KNOWN_MESSAGES_COUNT, wordsFromMessage[i - 1]).apply();
                        break;
                    case "MIN":
                        spEditor.putString(Constants.LAST_KNOWN_MINUTES_COUNT, wordsFromMessage[i - 1]).apply();
                        break;
                }
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