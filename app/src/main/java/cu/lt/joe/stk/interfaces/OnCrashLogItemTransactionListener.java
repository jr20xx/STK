package cu.lt.joe.stk.interfaces;

import cu.lt.joe.stk.objects.crash_log.CrashLog;

public interface OnCrashLogItemTransactionListener
{
    void onCrashLogItemAdded();

    void onCrashLogItemDeleted(CrashLog crashLog);
}
