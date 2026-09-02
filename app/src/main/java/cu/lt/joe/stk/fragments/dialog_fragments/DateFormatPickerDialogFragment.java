package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.Arrays;
import java.util.List;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;

public class DateFormatPickerDialogFragment extends DialogFragment
{
    private SharedPreferences sharp;
    private SharedPreferences.Editor preferencesEditor;
    private int currentlySelectedItemIndex;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        sharp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preferencesEditor = sharp.edit();

        List<String> dateFormatsLabels = Arrays.asList(getResources().getStringArray(R.array.date_formats_labels));
        List<String> dateFormats = Arrays.asList(getResources().getStringArray(R.array.date_formats));

        currentlySelectedItemIndex = dateFormats.indexOf(sharp.getString(Constants.SAVED_DATE_FORMAT, dateFormats.get(0)));

        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.date_format_title)
                .setSingleChoiceItems(dateFormatsLabels.toArray(new String[0]), currentlySelectedItemIndex, (dialog, which) -> currentlySelectedItemIndex = which)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    preferencesEditor.putString(Constants.SAVED_DATE_FORMAT, dateFormats.get(currentlySelectedItemIndex)).apply();
                    dismissAllowingStateLoss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}