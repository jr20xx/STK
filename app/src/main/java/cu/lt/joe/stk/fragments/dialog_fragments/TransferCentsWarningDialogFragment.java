package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TransferCentsWarningDialogFragment extends DialogFragment
{
    public static final String DIALOG_REQUEST_KEY = "TRANSFER_CENTS_WARNING_DIALOG_RQ",
            RESULT_KEY = "TRANSFER_CENTS_WARNING_DIALOG_RESULT";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Monto con centavos")
                .setMessage("Debido a restricciones que impiden la transferencia de saldo con centavos directamente desde aplicaciones de terceros, se ignorará la cantidad especificada y deberá introducirla cuando ETECSA le solicite el monto a transferir")
                .setPositiveButton(android.R.string.ok, (dialogInterface, which) -> {
                    Bundle result = new Bundle();
                    result.putBoolean(RESULT_KEY, true);
                    getParentFragmentManager().setFragmentResult(DIALOG_REQUEST_KEY, result);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}