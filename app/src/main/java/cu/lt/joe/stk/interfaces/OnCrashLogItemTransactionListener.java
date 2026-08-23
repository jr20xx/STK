package cu.lt.joe.stk.interfaces;

import cu.lt.joe.stk.objects.CrashLog;

public interface OnCrashLogItemTransactionListener
{
    void onCrashLogItemAdded();

    void onCrashLogItemDeleted(CrashLog crashLog);
}
