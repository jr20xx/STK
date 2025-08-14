package cu.lt.joe.stk.fragments.dialog_fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.stk.databinding.TransferPinChangeDialogLayoutBinding;
import cu.lt.joe.stk.utils.Utils;

public class TransferPasswordChangeDialogFragment extends DialogFragment
{
    private static final String OLD_PASSWORD_SAVE_TAG = "SAVED_OLD_PASSWORD",
            NEW_PASSWORD_SAVE_TAG = "SAVED_NEW_PASSWORD";
    private TransferPinChangeDialogLayoutBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        binding = TransferPinChangeDialogLayoutBinding.inflate(getLayoutInflater());
        binding.transferPasswordChangeOkButton.setOnClickListener(v -> {
            String oldPassword = binding.oldPasswordInputText.getEditText().getText().toString(),
                    newPassword = binding.newPasswordInputText.getEditText().getText().toString();
            binding.oldPasswordInputText.setErrorEnabled(true);
            if (oldPassword.isBlank() || oldPassword.length() < 4)
                binding.oldPasswordInputText.setError("La clave antigua debe tener cuatro dígitos");
            else
            {
                binding.oldPasswordInputText.setError(null);
                binding.oldPasswordInputText.setErrorEnabled(false);
                binding.newPasswordInputText.setErrorEnabled(true);
                if (newPassword.isBlank() || newPassword.length() < 4)
                    binding.newPasswordInputText.setError("La nueva clave debe tener cuatro dígitos");
                else if (newPassword.equals(oldPassword))
                    binding.newPasswordInputText.setError("La nueva clave debe ser diferente");
                else
                {
                    binding.newPasswordInputText.setError(null);
                    binding.newPasswordInputText.setErrorEnabled(false);
                    Utils.performCall(this, Uri.parse("tel:*234*2*" + oldPassword + "*" + newPassword + Uri.encode("#")));
                    dismissAllowingStateLoss();
                }
            }
        });
        binding.transferPasswordChangeCancelButton.setOnClickListener(v -> dismissAllowingStateLoss());

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(OLD_PASSWORD_SAVE_TAG))
                binding.oldPasswordInputText.getEditText().setText(savedInstanceState.getString(OLD_PASSWORD_SAVE_TAG));
            if (savedInstanceState.containsKey(NEW_PASSWORD_SAVE_TAG))
                binding.newPasswordInputText.getEditText().setText(savedInstanceState.getString(NEW_PASSWORD_SAVE_TAG));
        }
        return new MaterialAlertDialogBuilder(requireActivity()).setView(binding.getRoot()).create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        String oldPassword = binding.oldPasswordInputText.getEditText().getText().toString(),
                newPassword = binding.newPasswordInputText.getEditText().getText().toString();
        if (!oldPassword.isBlank())
            outState.putString(OLD_PASSWORD_SAVE_TAG, oldPassword);
        if (!newPassword.isBlank())
            outState.putString(NEW_PASSWORD_SAVE_TAG, newPassword);
        super.onSaveInstanceState(outState);
    }
}