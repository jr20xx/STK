package cu.lt.joe.stk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import java.io.PrintWriter;
import java.io.StringWriter;
import cu.lt.joe.stk.activities.MainActivity;

public class AppCore extends Application
{
    public static final String ERROR_TAG = "ERROR_MESSAGE_TAG";
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    public void onCreate()
    {
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC, 1000,
                    PendingIntent.getActivity(getApplicationContext(), 111, getResurrectionIntent(throwable), PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE));

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            uncaughtExceptionHandler.uncaughtException(thread, throwable);
        });
        super.onCreate();
    }

    @NonNull
    private Intent getResurrectionIntent(@NonNull Throwable throwable)
    {
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        for (; throwable != null; throwable = throwable.getCause())
            throwable.printStackTrace(printWriter);
        return new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(ERROR_TAG, result.toString());
    }
}