package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.utils.Utils;

public class LanguagePickerDialogFragment extends DialogFragment
{
    private int currentlySelectedItemIndex;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        ArrayList<String> supportedLocales = new ArrayList<>();
        supportedLocales.add(getString(R.string.default_language_tag));
        supportedLocales.add("Español");
        supportedLocales.add("English");

        String currentLanguageTag = AppCompatDelegate.getApplicationLocales().toLanguageTags().toLowerCase();
        int defaultCheckedItem = 0;
        if (currentLanguageTag.startsWith(Constants.LANGUAGE_TAGS[1]))
            defaultCheckedItem = 1;
        else if (currentLanguageTag.startsWith(Constants.LANGUAGE_TAGS[2]))
            defaultCheckedItem = 2;

        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.languages_dialog_title)
                .setSingleChoiceItems(supportedLocales.toArray(new String[0]), defaultCheckedItem, (dialog, which) -> currentlySelectedItemIndex = which)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Utils.updateAppLocale(Constants.LANGUAGE_TAGS[currentlySelectedItemIndex]);
                    dismissAllowingStateLoss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}