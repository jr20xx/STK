package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;

public class LanguagePickerDialogFragment extends DialogFragment
{
    private SharedPreferences sharp;
    private SharedPreferences.Editor editor;
    private int currentlySelectedItemIndex;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        sharp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        editor = sharp.edit();

        ArrayList<String> supportedLocales = new ArrayList<>();
        supportedLocales.add(getString(R.string.default_language_tag));
        supportedLocales.add("Español");
        supportedLocales.add("English");
        int defaultCheckedItem = sharp.getInt(Constants.SAVED_LANGUAGE_INDEX, 0);
        if (savedInstanceState != null)
            defaultCheckedItem = savedInstanceState.getInt(Constants.SAVED_LANGUAGE_INDEX, defaultCheckedItem);
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.languages_dialog_title)
                .setSingleChoiceItems(supportedLocales.toArray(new String[0]), defaultCheckedItem, (dialog, which) -> currentlySelectedItemIndex = which)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String languageTag = null;
                    switch (currentlySelectedItemIndex)
                    {
                        case 1:
                            languageTag = "es";
                            break;
                        case 2:
                            languageTag = "en";
                    }
                    if (languageTag != null)
                    {
                        editor.putInt(Constants.SAVED_LANGUAGE_INDEX, currentlySelectedItemIndex).apply();
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag));
                        dismissAllowingStateLoss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(Constants.SAVED_LANGUAGE_INDEX, currentlySelectedItemIndex);
        super.onSaveInstanceState(outState);
    }
}