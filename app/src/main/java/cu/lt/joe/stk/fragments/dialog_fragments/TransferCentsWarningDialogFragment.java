package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.stk.R;

public class TransferCentsWarningDialogFragment extends DialogFragment
{
    public static final String DIALOG_REQUEST_KEY = "TRANSFER_CENTS_WARNING_DIALOG_RQ",
            RESULT_KEY = "TRANSFER_CENTS_WARNING_DIALOG_RESULT";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.transfer_cents_dialog_title)
                .setMessage(R.string.transfer_cents_dialog_message)
                .setPositiveButton(android.R.string.ok, (dialogInterface, which) -> {
                    Bundle result = new Bundle();
                    result.putBoolean(RESULT_KEY, true);
                    getParentFragmentManager().setFragmentResult(DIALOG_REQUEST_KEY, result);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}