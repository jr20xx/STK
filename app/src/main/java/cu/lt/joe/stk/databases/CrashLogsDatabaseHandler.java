package cu.lt.joe.stk.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import cu.lt.joe.stk.interfaces.OnCrashLogItemTransactionListener;
import cu.lt.joe.stk.objects.CrashLog;

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

    public CrashLogsDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public ArrayList<CrashLog> getCrashLogs()
    {
        ArrayList<CrashLog> logs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CRASH_LOGS_TABLE + " ORDER BY " + TABLE_TIMESTAMP_ROW + " DESC;", null);

        int idRowIndex = cursor.getColumnIndex(TABLE_ID_ROW);
        int errorTitleRowIndex = cursor.getColumnIndex(TABLE_ERROR_TITLE_ROW);
        int errorMessageRowIndex = cursor.getColumnIndex(TABLE_ERROR_MESSAGE_ROW);
        int timestampRowIndex = cursor.getColumnIndex(TABLE_TIMESTAMP_ROW);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            logs.add(new CrashLog(cursor.getLong(idRowIndex), cursor.getString(errorTitleRowIndex), cursor.getString(errorMessageRowIndex), cursor.getLong(timestampRowIndex)));
        return logs;
    }

    public void setOnLogItemTransactionListener(OnCrashLogItemTransactionListener onCrashLogItemTransactionListener)
    {
        this.onCrashLogItemTransactionListener = onCrashLogItemTransactionListener;
    }
}