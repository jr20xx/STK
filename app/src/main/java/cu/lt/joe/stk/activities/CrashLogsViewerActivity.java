package cu.lt.joe.stk.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import java.text.SimpleDateFormat;
import java.util.Locale;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.adapters.CrashLogsAdapter;
import cu.lt.joe.stk.databases.CrashLogsDatabaseHandler;
import cu.lt.joe.stk.databinding.CrashLogsViewerActivityLayoutBinding;

public class CrashLogsViewerActivity extends BaseActivity
{
    private CrashLogsViewerActivityLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.crash_logs_viewer_activity_layout);
        binding.crashLogsViewerActivityRv.setAdapter(new CrashLogsAdapter(this,
                new CrashLogsDatabaseHandler(this).getCrashLogs(),
                new SimpleDateFormat("yyyy/MM/dd, hh:mm a", Locale.getDefault())));
    }
}