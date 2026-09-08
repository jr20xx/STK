package cu.lt.joe.stk.fragments.additional_fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.ChangeSimCardPinLayoutBinding;
import cu.lt.joe.stk.utils.Utils;

public class ChangeSIMCardPinFragment extends BottomSheetDialogFragment
{
    private final String CURRENT_PIN_SAVE_TAG = "SAVED_CURRENT_PIN",
            NEW_PIN_SAVE_TAG = "SAVED_NEW_PIN",
            NEW_PIN_REPETITION_SAVE_TAG = "SAVED_PIN_REPETITION";
    private ChangeSimCardPinLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = ChangeSimCardPinLayoutBinding.inflate(inflater, container, false);

        binding.simCardPinChangeTogglePinPuk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.currentAccessCodeInputText.setHint(isChecked ? R.string.puk_code_hint : R.string.current_pin_hint);
            Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText, binding.newPinInputText, binding.newPinRepetitionInputText);
        });

        binding.currentAccessCodeInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText);
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
                Utils.clearErrorsOnTextInputLayouts(binding.newPinInputText);
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
                Utils.clearErrorsOnTextInputLayouts(binding.newPinRepetitionInputText);
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
                binding.currentAccessCodeInputText.setError(getString(R.string.puk_length_error));
                Utils.clearErrorsOnTextInputLayouts(binding.newPinInputText, binding.newPinRepetitionInputText);
            }
            else if (!binding.simCardPinChangeTogglePinPuk.isChecked() && currentCode.length() < 4)
            {
                binding.currentAccessCodeInputText.setError(getString(R.string.short_pin_error));
                Utils.clearErrorsOnTextInputLayouts(binding.newPinInputText, binding.newPinRepetitionInputText);
            }
            else if (newPIN.length() < 4)
            {
                binding.newPinInputText.setError(getString(R.string.short_new_pin_error));
                Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText, binding.newPinRepetitionInputText);
            }
            else if (newPIN.equals(currentCode))
            {
                binding.newPinInputText.setError(getString(R.string.pin_replacement_is_old_pin_error));
                Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText, binding.newPinRepetitionInputText);
            }
            else if (!newPINRepetition.equals(newPIN))
            {
                binding.newPinRepetitionInputText.setError(getString(R.string.pin_repetition_mismatch));
                Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText, binding.newPinInputText);
            }
            else
            {
                Utils.clearErrorsOnTextInputLayouts(binding.currentAccessCodeInputText, binding.newPinInputText, binding.newPinRepetitionInputText);
                Utils.performCallFromFragment(this, Uri.fromParts("tel",
                        (binding.simCardPinChangeTogglePinPuk.isChecked() ? "**05*" : "**04*")
                                + currentCode + "*" + newPIN + "*" + newPINRepetition + "#", null));
                dismissAllowingStateLoss();
            }
        });

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(CURRENT_PIN_SAVE_TAG))
                binding.currentAccessCodeInputText.getEditText().setText(savedInstanceState.getString(CURRENT_PIN_SAVE_TAG));
            if (savedInstanceState.containsKey(NEW_PIN_SAVE_TAG))
                binding.newPinInputText.getEditText().setText(savedInstanceState.getString(NEW_PIN_SAVE_TAG));
            if (savedInstanceState.containsKey(NEW_PIN_REPETITION_SAVE_TAG))
                binding.newPinRepetitionInputText.getEditText().setText(savedInstanceState.getString(NEW_PIN_REPETITION_SAVE_TAG));
        }
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        String currentPIN = binding.currentAccessCodeInputText.getEditText().getText().toString(),
                newPIN = binding.newPinInputText.getEditText().getText().toString(),
                newPINRepetition = binding.newPinRepetitionInputText.getEditText().getText().toString();
        if (!currentPIN.isBlank())
            outState.putString(CURRENT_PIN_SAVE_TAG, currentPIN);
        if (!newPIN.isBlank())
            outState.putString(NEW_PIN_SAVE_TAG, newPIN);
        if (!newPINRepetition.isBlank())
            outState.putString(NEW_PIN_REPETITION_SAVE_TAG, newPINRepetition);
        super.onSaveInstanceState(outState);
    }
}