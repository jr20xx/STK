package cu.lt.joe.stk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import cu.lt.joe.stk.activities.MainActivity;
import cu.lt.joe.stk.databases.CrashLogsDatabaseHandler;
import cu.lt.joe.stk.objects.CrashLog;

public class AppCore extends Application
{
    public static final String ERROR_TAG = "ERROR_MESSAGE_TAG";
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    public void onCreate()
    {
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            CrashLog crashLog = getCrashLog(throwable);
            new CrashLogsDatabaseHandler(this).addLog(crashLog.getTitle(), crashLog.getBody(), crashLog.getTimestamp());

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            uncaughtExceptionHandler.uncaughtException(thread, throwable);
        });
        super.onCreate();
    }

    private @NonNull CrashLog getCrashLog(@NonNull Throwable throwable)
    {
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        for (Throwable iterableThrowable = throwable; iterableThrowable != null; iterableThrowable = iterableThrowable.getCause())
            iterableThrowable.printStackTrace(printWriter);
        return new CrashLog(-1, throwable.getClass().getSimpleName(), result.toString(), new Date().getTime());
    }
}