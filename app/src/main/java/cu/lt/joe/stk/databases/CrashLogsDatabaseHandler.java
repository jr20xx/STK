package cu.lt.joe.stk.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.preference.PreferenceManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.interfaces.OnCrashLogItemTransactionListener;
import cu.lt.joe.stk.objects.CrashLog;
import cu.lt.joe.stk.objects.CrashLogsGroup;

public class CrashLogsDatabaseHandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "crash_logs.db",
            CRASH_LOGS_TABLE = "crash_logs",
            TABLE_ID_ROW = "ID",
            TABLE_ERROR_TITLE_ROW = "error_title",
            TABLE_ERROR_MESSAGE_ROW = "error_message",
            TABLE_TIMESTAMP_ROW = "timestamp";
    private static final int DATABASE_VERSION = 1;
    private OnCrashLogItemTransactionListener onCrashLogItemTransactionListener;
    private Context context;

    public CrashLogsDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + CRASH_LOGS_TABLE + "(" +
                TABLE_ID_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_ERROR_TITLE_ROW + " TEXT NOT NULL," +
                TABLE_ERROR_MESSAGE_ROW + " TEXT NOT NULL," +
                TABLE_TIMESTAMP_ROW + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }

    public void addLog(String errorTitle, String errorMessage, long timestamp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_ERROR_TITLE_ROW, errorTitle);
        cv.put(TABLE_ERROR_MESSAGE_ROW, errorMessage);
        cv.put(TABLE_TIMESTAMP_ROW, timestamp);
        db.insert(CRASH_LOGS_TABLE, null, cv);
        if (onCrashLogItemTransactionListener != null)
            onCrashLogItemTransactionListener.onCrashLogItemAdded();
    }

    public void deleteLog(CrashLog item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete(CRASH_LOGS_TABLE, TABLE_ID_ROW + "=" + item.getID(), null) > 0 && onCrashLogItemTransactionListener != null)
            onCrashLogItemTransactionListener.onCrashLogItemDeleted(item);
        db.close();
    }

    public ArrayList<CrashLogsGroup> getCrashLogsGroups()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CRASH_LOGS_TABLE + " ORDER BY " + TABLE_TIMESTAMP_ROW + " DESC;", null);

        int idRowIndex = cursor.getColumnIndex(TABLE_ID_ROW);
        int errorTitleRowIndex = cursor.getColumnIndex(TABLE_ERROR_TITLE_ROW);
        int errorMessageRowIndex = cursor.getColumnIndex(TABLE_ERROR_MESSAGE_ROW);
        int timestampRowIndex = cursor.getColumnIndex(TABLE_TIMESTAMP_ROW);

        ArrayList<CrashLogsGroup> crashLogsGroups = new ArrayList<>();
        ArrayList<CrashLog> logs = new ArrayList<>();
        CrashLogsGroup crashLogsGroup;
        String previousFormattedDate = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            long currentTimestamp = cursor.getLong(timestampRowIndex);
            String currentFormattedDate = new SimpleDateFormat(PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SAVED_DATE_FORMAT, context.getResources().getStringArray(R.array.date_formats)[0]), Locale.getDefault()).format(new Date(currentTimestamp));
            if (!currentFormattedDate.equals(previousFormattedDate))
            {
                logs = new ArrayList<>();
                crashLogsGroup = new CrashLogsGroup(currentFormattedDate, logs);
                crashLogsGroups.add(crashLogsGroup);
            }
            logs.add(new CrashLog(cursor.getLong(idRowIndex), cursor.getString(errorTitleRowIndex), cursor.getString(errorMessageRowIndex), new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date(currentTimestamp))));
            previousFormattedDate = currentFormattedDate;
        }
        cursor.close();
        return crashLogsGroups;
    }

    public void setOnLogItemTransactionListener(OnCrashLogItemTransactionListener onCrashLogItemTransactionListener)
    {
        this.onCrashLogItemTransactionListener = onCrashLogItemTransactionListener;
    }
}