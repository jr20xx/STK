package cu.lt.joe.stk.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import androidx.preference.PreferenceManager;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.utils.Utils;

@SuppressLint("AccessibilityPolicy")
public class USSDAccessibilityService extends AccessibilityService
{
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        final String TERMINATED_DATA_PACKET_CONTENT_MESSAGE = "Ud debe adquirir una oferta",
                TERMINATED_SMS_PACKET_CONTENT_MESSAGE = "Usted debe adquirir un plan de SMS",
                TERMINATED_VOICE_PACKET_CONTENT_MESSAGE = "Usted debe adquirir un plan de minutos";

        SharedPreferences sharp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sharp.edit();

        if (event.getClassName() != null && AlertDialog.class.getName().contentEquals(event.getClassName()))
        {
            String responseMessage = Utils.extractTextFromAccessibilityEvent(event).toUpperCase();
            if (responseMessage.contains(TERMINATED_DATA_PACKET_CONTENT_MESSAGE.toUpperCase()))
                spEditor.putString(Constants.LAST_KNOWN_INTERNET_DATA, null).apply();
            else if (responseMessage.contains(TERMINATED_VOICE_PACKET_CONTENT_MESSAGE.toUpperCase()))
                spEditor.putString(Constants.LAST_KNOWN_MINUTES_COUNT, null).apply();
            else if (responseMessage.contains(TERMINATED_SMS_PACKET_CONTENT_MESSAGE.toUpperCase()))
                spEditor.putString(Constants.LAST_KNOWN_MESSAGES_COUNT, null).apply();
            else
            {
                String wordsFromMessage[] = responseMessage.toUpperCase().split(" ");
                for (int i = 0; i < wordsFromMessage.length; i++)
                {
                    String currentWord = wordsFromMessage[i];
                    switch (currentWord)
                    {
                        case "SALDO:":
                            spEditor.putString(Constants.LAST_KNOWN_BALANCE, wordsFromMessage[i + 1] + " " + wordsFromMessage[i + 2].replace(".", "")).apply();
                            spEditor.putString(Constants.LAST_KNOWN_INTERNET_DATA, null).apply();
                            spEditor.putString(Constants.LAST_KNOWN_MINUTES_COUNT, null).apply();
                            spEditor.putString(Constants.LAST_KNOWN_MESSAGES_COUNT, null).apply();
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