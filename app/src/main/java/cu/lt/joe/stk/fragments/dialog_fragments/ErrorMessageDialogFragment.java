package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.stk.AppCore;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.utils.Utils;

public class ErrorMessageDialogFragment extends DialogFragment
{
    private String errorMessage;

    @NonNull
    public static ErrorMessageDialogFragment newInstance(@NonNull String errorMessage)
    {
        ErrorMessageDialogFragment fragment = new ErrorMessageDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppCore.ERROR_TAG, errorMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        if (getArguments() != null)
            errorMessage = getArguments().getString(AppCore.ERROR_TAG);
        if (savedInstanceState != null && savedInstanceState.containsKey(AppCore.ERROR_TAG))
            errorMessage = savedInstanceState.getString(AppCore.ERROR_TAG);
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.recovery_mode_dialog_title)
                .setMessage(R.string.recovery_mode_dialog_message)
                .setPositiveButton(android.R.string.copy, (dialogInterface, which) ->
                        Utils.copyToClipboard(requireActivity(), getString(R.string.error_message_clipboard_tag), errorMessage))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putString(AppCore.ERROR_TAG, errorMessage);
        super.onSaveInstanceState(outState);
    }
}