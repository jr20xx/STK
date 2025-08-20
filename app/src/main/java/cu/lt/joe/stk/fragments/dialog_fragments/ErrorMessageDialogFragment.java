package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.stk.AppCore;
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
                .setTitle("Modo de recuperación")
                .setMessage("Ocurrió un error inesperado y la aplicación tuvo que reiniciarse. Puede copiar el mensaje de error para obtener detalles avanzados del error ocurrido o simplemente descartar este mensaje. Lamentamos las molestias ocasionadas y al cerrar este diálogo podrá continuar utilizando la aplicación con normalidad")
                .setPositiveButton(android.R.string.copy, (dialogInterface, which) ->
                        Utils.copyToClipboard(requireActivity(), "Mensaje de error de STK", errorMessage))
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