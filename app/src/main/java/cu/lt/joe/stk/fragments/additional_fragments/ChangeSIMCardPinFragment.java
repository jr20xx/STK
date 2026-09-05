package cu.lt.joe.stk.fragments.additional_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.ChangeSimCardPinLayoutBinding;

public class ChangeSIMCardPinFragment extends Fragment
{
    private ChangeSimCardPinLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = ChangeSimCardPinLayoutBinding.inflate(inflater, container, false);

        binding.simCardPinChangeTogglePinPuk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.currentAccessCodeInputText.setHint(isChecked ? R.string.puk_code_hint : R.string.current_pin_hint);
            clearTextInputLayoutsErrors(binding.currentAccessCodeInputText, binding.newPinInputText, binding.newPinRepetitionInputText);
        });

        binding.currentAccessCodeInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                clearTextInputLayoutsErrors(binding.currentAccessCodeInputText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
        binding.newPinInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                clearTextInputLayoutsErrors(binding.newPinInputText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
        binding.newPinRepetitionInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                clearTextInputLayoutsErrors(binding.newPinRepetitionInputText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        binding.simCardPinChangeOkButton.setOnClickListener(v -> {
            String currentCode = binding.currentAccessCodeInputText.getEditText().getText().toString(),
                    newPIN = binding.newPinInputText.getEditText().getText().toString(),
                    newPINRepetition = binding.newPinRepetitionInputText.getEditText().getText().toString();
            if (binding.simCardPinChangeTogglePinPuk.isChecked() && currentCode.length() < 8)
            {
                binding.currentAccessCodeInputText.setError("El PUK debe tener 8 dígitos");
                clearTextInputLayoutsErrors(binding.newPinInputText, binding.newPinRepetitionInputText);
            }
            else if (!binding.simCardPinChangeTogglePinPuk.isChecked() && currentCode.length() < 4)
            {
                binding.currentAccessCodeInputText.setError("El PIN debe tener al menos 4 dígitos");
                clearTextInputLayoutsErrors(binding.newPinInputText, binding.newPinRepetitionInputText);
            }
            else if (newPIN.length() < 4)
            {
                binding.newPinInputText.setError("El nuevo PIN debe tener al menos 4 dígitos");
                clearTextInputLayoutsErrors(binding.currentAccessCodeInputText, binding.newPinRepetitionInputText);
            }
            else if (newPIN.equals(currentCode))
            {
                binding.newPinInputText.setError("El nuevo PIN debe ser diferente al anterior");
                clearTextInputLayoutsErrors(binding.currentAccessCodeInputText, binding.newPinRepetitionInputText);
            }
            else if (!newPINRepetition.equals(newPIN))
            {
                binding.newPinRepetitionInputText.setError("Los códigos PIN no coinciden");
                clearTextInputLayoutsErrors(binding.currentAccessCodeInputText, binding.newPinInputText);
            }
            else
            {
                clearTextInputLayoutsErrors(binding.currentAccessCodeInputText, binding.newPinInputText, binding.newPinRepetitionInputText);
            }
        });

        return binding.getRoot();
    }

    private void clearTextInputLayoutsErrors(TextInputLayout... textInputLayouts)
    {
        for (TextInputLayout textInputLayout : textInputLayouts)
        {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
    }
}