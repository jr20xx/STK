package cu.lt.joe.stk.fragments.additional_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Locale;
import cu.lt.joe.stk.adapters.CrashLogsAdapter;
import cu.lt.joe.stk.databases.CrashLogsDatabaseHandler;
import cu.lt.joe.stk.databinding.CrashLogsViewerLayoutBinding;

public class CrashLogsViewerFragment extends Fragment
{
    private CrashLogsViewerLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = CrashLogsViewerLayoutBinding.inflate(inflater, container, false);
        binding.crashLogsViewerRv.setAdapter(new CrashLogsAdapter(requireContext(),
                new CrashLogsDatabaseHandler(requireContext()).getCrashLogs(),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
        return binding.getRoot();
    }
}