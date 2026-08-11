package cu.lt.joe.stk.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import java.util.Arrays;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.SettingsFragmentBinding;
import cu.lt.joe.stk.utils.Utils;

public class SettingsFragment extends Fragment
{
    private static final String SCROLL_POSITION_SAVER_TAG = "SAVED_SCROLL_POSITION";
    private SettingsFragmentBinding binding;
    private SharedPreferences sharp;
    private SharedPreferences.Editor preferencesEditor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        sharp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preferencesEditor = sharp.edit();
        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        binding.setLinkOpener(this);
        binding.accessibilityServiceSwitchCard.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        binding.revertedCallPrefixSelectorSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                R.layout.simple_spinner_dropdown_item,
                Arrays.asList(Constants.REVERTED_CALL_PREFIXES_ARRAY)));
        binding.revertedCallPrefixSelectorCard.setOnClickListener(v -> binding.revertedCallPrefixSelectorSpinner.performClick());
        binding.revertedCallPrefixSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                preferencesEditor.putInt(Constants.REVERTED_CALL_PREFIX_INDEX, position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        binding.revertedCallPrefixSelectorSpinner.setSelection(sharp.getInt(Constants.REVERTED_CALL_PREFIX_INDEX, 0));
        return binding.getRoot();
    }

    public void openExternalLink(String webLink)
    {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webLink)));
        }
        catch (Exception e)
        {
            Snackbar.make(binding.getRoot(), R.string.opening_link_error_tip, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(SCROLL_POSITION_SAVER_TAG, binding.infoFragmentScrollview.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
            binding.infoFragmentScrollview.setScrollY(savedInstanceState.getInt(SCROLL_POSITION_SAVER_TAG));
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        binding.accessibilityServiceSwitch.setChecked(Utils.isAccessibilityServiceEnabled(requireContext()));
    }
}