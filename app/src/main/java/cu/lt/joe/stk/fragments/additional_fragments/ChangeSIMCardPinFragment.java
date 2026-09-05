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
            clearAllTextLayoutErrors();
        });

        binding.currentAccessCodeInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                clearInputLayoutError(binding.currentAccessCodeInputText);
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
                clearInputLayoutError(binding.newPinInputText);
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
                clearInputLayoutError(binding.newPinRepetitionInputText);
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
                clearInputLayoutError(binding.newPinInputText);
                clearInputLayoutError(binding.newPinRepetitionInputText);
            }
            else if (!binding.simCardPinChangeTogglePinPuk.isChecked() && currentCode.length() < 4)
            {
                binding.currentAccessCodeInputText.setError("El PIN debe tener al menos 4 dígitos");
                clearInputLayoutError(binding.newPinInputText);
                clearInputLayoutError(binding.newPinRepetitionInputText);
            }
            else if (newPIN.length() < 4)
            {
                binding.newPinInputText.setError("El nuevo PIN debe tener al menos 4 dígitos");
                clearInputLayoutError(binding.currentAccessCodeInputText);
                clearInputLayoutError(binding.newPinRepetitionInputText);
            }
            else if (newPIN.equals(currentCode))
            {
                binding.newPinInputText.setError("El nuevo PIN debe ser diferente al anterior");
                clearInputLayoutError(binding.currentAccessCodeInputText);
                clearInputLayoutError(binding.newPinRepetitionInputText);
            }
            else if (!newPINRepetition.equals(newPIN))
            {
                binding.newPinRepetitionInputText.setError("Los códigos PIN no coinciden");
                clearInputLayoutError(binding.currentAccessCodeInputText);
                clearInputLayoutError(binding.newPinInputText);
            }
            else
            {
                clearAllTextLayoutErrors();
            }
        });

        return binding.getRoot();
    }

    private void clearAllTextLayoutErrors()
    {
        binding.currentAccessCodeInputText.setError(null);
        binding.currentAccessCodeInputText.setErrorEnabled(false);
        binding.newPinInputText.setError(null);
        binding.newPinInputText.setErrorEnabled(false);
        binding.newPinRepetitionInputText.setError(null);
        binding.newPinRepetitionInputText.setErrorEnabled(false);
    }

    private void clearInputLayoutError(TextInputLayout textInputLayout)
    {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }
}