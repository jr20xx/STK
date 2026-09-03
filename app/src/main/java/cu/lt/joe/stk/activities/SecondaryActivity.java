package cu.lt.joe.stk.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.SecondaryActivityLayoutBinding;
import cu.lt.joe.stk.fragments.additional_fragments.ChangeSIMCardPinFragment;
import cu.lt.joe.stk.fragments.additional_fragments.CrashLogsViewerFragment;
import cu.lt.joe.stk.fragments.additional_fragments.SettingsFragment;

public class SecondaryActivity extends BaseActivity
{
    private SecondaryActivityLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.secondary_activity_layout);

        int titleId = getIntent().getIntExtra(Intent.EXTRA_TITLE, R.string.app_name);
        binding.secondaryActivityTitleTv.setText(titleId);
        if (titleId == R.string.settings_fragment_title)
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.secondaryActivityFrameLayout.getId(), new SettingsFragment())
                    .commit();
        else if (titleId == R.string.crash_logs_fragment_title)
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.secondaryActivityFrameLayout.getId(), new CrashLogsViewerFragment())
                    .commit();
        else if (titleId == R.string.change_sim_pin_title)
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.secondaryActivityFrameLayout.getId(), new ChangeSIMCardPinFragment())
                    .commit();
        else finish();
    }
}