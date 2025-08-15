package cu.lt.joe.stk.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.Objects;
import cu.lt.joe.stk.activities.VoucherCodeScannerActivity;
import cu.lt.joe.stk.databinding.MainFragmentBinding;
import cu.lt.joe.stk.fragments.dialog_fragments.TransferPasswordChangeDialogFragment;
import cu.lt.joe.stk.utils.Utils;

public class MainFragment extends Fragment
{
    private final String TRIGGER_ID_SAVE_TAG = "SAVED_CURRENT_TRIGGER_VIEW", VOUCHER_CODE_SAVE_TAG = "SAVED_VOUCHER_CODE",
            TRANSFER_PHONE_NUMBER_SAVE_TAG = "SAVED_TRANSFER_PHONE_NUMBER", TRANSFER_PASSWORD_SAVE_TAG = "SAVED_TRANSFER_PASSWORD_VIEW",
            TRANSFER_BALANCE_VIEW_TAG = "SAVED_TRANSFER_BALANCE_VIEW", SCROLL_POSITION_SAVE_TAG = "SAVED_SCROLL_POSITION";
    private View triggerView;
    private MainFragmentBinding binding;
    private final ActivityResultLauncher<ScanOptions> voucherActivationCodeBarcodeScanner = registerForActivityResult(new ScanContract(),
            result ->
            {
                String obtainedContents = result.getContents();
                if (obtainedContents != null)
                {
                    if (obtainedContents.isBlank())
                        Snackbar.make(binding.getRoot(), "Código inválido", Snackbar.LENGTH_SHORT).show();
                    else
                    {
                        obtainedContents = obtainedContents.trim();
                        for (char c : obtainedContents.toCharArray())
                            if (c < '0' || c > '9')
                            {
                                Snackbar.make(binding.getRoot(), "Código con caracteres inválidos", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        binding.voucherCodeInputText.getEditText().setText(obtainedContents);
                    }
                }
            });
    private final ActivityResultLauncher<Intent> contactPickerResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == FragmentActivity.RESULT_OK && result.getData() != null)
                {
                    Uri contactUri = result.getData().getData();
                    try (Cursor cursor = requireActivity().getContentResolver().query(contactUri,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            null, null, null))
                    {
                        if (cursor != null && cursor.moveToFirst())
                        {
                            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(cursor.getString(0), "CU");
                            String numberAsString = phoneNumberUtil.getNationalSignificantNumber(number);
                            if (!phoneNumberUtil.isValidNumberForRegion(number, "CU") || numberAsString.charAt(0) != '5' && numberAsString.charAt(0) != '6')
                                Snackbar.make(binding.getRoot(), "El número asociado es inválido", Snackbar.LENGTH_SHORT).show();
                            else
                            {
                                if (triggerView != null)
                                {
                                    if (triggerView.equals(binding.privateCallCard))
                                        Utils.performCall(this, Uri.parse("tel:" + Uri.encode("#") + "31" + Uri.encode("#") + numberAsString));
                                    else if (triggerView.equals(binding.revertedCallCard))
                                        Utils.performCall(this, Uri.parse("tel:*99" + numberAsString));
                                    else
                                        binding.transferPhoneNumberInputText.getEditText().setText(numberAsString);
                                }
                            }
                        }
                    }
                    catch (Exception ignored)
                    {
                        Snackbar.make(binding.getRoot(), "El número elegido no se puede usar", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        binding.setUSSDRequester(this);
        binding.voucherCodeInputText.setEndIconOnClickListener(v ->
                voucherActivationCodeBarcodeScanner.launch(new ScanOptions()
                        .setCaptureActivity(VoucherCodeScannerActivity.class)
                        .setBeepEnabled(false).setDesiredBarcodeFormats(ScanOptions.QR_CODE)));
        binding.transferPhoneNumberInputText.setEndIconOnClickListener(v ->
        {
            triggerView = v;
            launchContactPicker();
        });
        Objects.requireNonNull(binding.voucherCodeInputText.getEditText()).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isBlank() || binding.voucherCodeInputText.isErrorEnabled())
                {
                    binding.voucherCodeInputText.setError(null);
                    binding.voucherCodeInputText.setErrorEnabled(false);
                }
            }
        });
        Objects.requireNonNull(binding.transferPhoneNumberInputText.getEditText()).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isBlank() || binding.transferPhoneNumberInputText.isErrorEnabled())
                {
                    binding.transferPhoneNumberInputText.setError(null);
                    binding.transferPhoneNumberInputText.setErrorEnabled(false);
                }
            }
        });
        binding.transferPasswordInputText.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isBlank() || binding.transferPasswordInputText.isErrorEnabled())
                {
                    binding.transferPasswordInputText.setError(null);
                    binding.transferPasswordInputText.setErrorEnabled(false);
                }
            }
        });
        Objects.requireNonNull(binding.transferBalanceInputText.getEditText()).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isBlank() || binding.transferBalanceInputText.isErrorEnabled())
                {
                    binding.transferBalanceInputText.setError(null);
                    binding.transferBalanceInputText.setErrorEnabled(false);
                }
            }
        });
        return binding.getRoot();
    }

    public void executeUSSDRequest(View v)
    {
        if (v.equals(binding.balanceCard))
            Utils.performCall(this, Uri.parse("tel:*222" + Uri.encode("#")));
        else if (v.equals(binding.dataCard))
            Utils.performCall(this, Uri.parse("tel:*222*328" + Uri.encode("#")));
        else if (v.equals(binding.messagesCard))
            Utils.performCall(this, Uri.parse("tel:*222*767" + Uri.encode("#")));
        else if (v.equals(binding.callsCard))
            Utils.performCall(this, Uri.parse("tel:*222*869" + Uri.encode("#")));
        else if (v.equals(binding.bonusesCard))
            Utils.performCall(this, Uri.parse("tel:*222*266" + Uri.encode("#")));
        else if (v.equals(binding.limitsCard))
            Utils.performCall(this, Uri.parse("tel:*222*732" + Uri.encode("#")));
        else if (v.equals(binding.rechargeButton))
        {
            String voucherActivationCode = binding.voucherCodeInputText.getEditText().getText().toString();
            binding.voucherCodeInputText.setErrorEnabled(true);
            if (voucherActivationCode.isBlank() || voucherActivationCode.length() < 16)
                binding.voucherCodeInputText.setError("El código debe tener 16 dígitos");
            else
            {
                binding.voucherCodeInputText.setError(null);
                binding.voucherCodeInputText.setErrorEnabled(false);
                Utils.performCall(this, Uri.parse("tel:*662*" + voucherActivationCode + Uri.encode("#")));
                binding.voucherCodeInputText.getEditText().setText(null);
            }
        }
        else if (v.equals(binding.transferButton))
        {
            String receiverNumber = binding.transferPhoneNumberInputText.getEditText().getText().toString(),
                    passwordCode = binding.transferPasswordInputText.getEditText().getText().toString(),
                    balanceAmount = binding.transferBalanceInputText.getEditText().getText().toString();
            binding.transferPhoneNumberInputText.setErrorEnabled(true);
            if (receiverNumber.isBlank() || receiverNumber.length() < 8)
                binding.transferPhoneNumberInputText.setError("El número del receptor debe tener 8 dígitos");
            else if (receiverNumber.charAt(0) != '5' && receiverNumber.charAt(0) != '6')
                binding.transferPhoneNumberInputText.setError("El número del receptor debe empezar con 5 o 6");
            else
            {
                binding.transferPhoneNumberInputText.setError(null);
                binding.transferPhoneNumberInputText.setErrorEnabled(false);
                binding.transferPasswordInputText.setErrorEnabled(true);
                if (passwordCode.isBlank() || passwordCode.length() < 4)
                    binding.transferPasswordInputText.setError("La clave debe contener 4 dígitos");
                else
                {
                    binding.transferPasswordInputText.setError(null);
                    binding.transferPasswordInputText.setErrorEnabled(false);
                    binding.transferBalanceInputText.setErrorEnabled(true);
                    if (balanceAmount.isBlank())
                        binding.transferBalanceInputText.setError("Introduzca el monto");
                    else if (Double.parseDouble(balanceAmount) == 0)
                        binding.transferBalanceInputText.setError("El monto a transferir no puede ser 0");
                    else
                    {
                        binding.transferBalanceInputText.setError(null);
                        binding.transferBalanceInputText.setErrorEnabled(false);
                        if (balanceAmount.contains(".") || balanceAmount.contains(","))
                        {
                            new MaterialAlertDialogBuilder(requireActivity())
                                    .setTitle("Monto con centavos")
                                    .setMessage("Debido a restricciones que impiden la transferencia de saldo con centavos directamente desde aplicaciones de terceros, se ignorará la cantidad especificada y deberá introducirla cuando ETECSA le solicite el monto a transferir")
                                    .setPositiveButton(android.R.string.ok, (dialogInterface, which) -> {
                                        Utils.performCall(this, Uri.parse("tel:*234*1*" + receiverNumber + "*" + passwordCode + Uri.encode("#")));
                                        binding.transferPhoneNumberInputText.getEditText().setText(null);
                                        binding.transferPasswordInputText.getEditText().setText(null);
                                        binding.transferBalanceInputText.getEditText().setText(null);
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .show();
                        }
                        else
                        {
                            Utils.performCall(this, Uri.parse("tel:*234*1*" + receiverNumber + "*" + passwordCode + "*" + balanceAmount + Uri.encode("#")));
                            binding.transferPhoneNumberInputText.getEditText().setText(null);
                            binding.transferPasswordInputText.getEditText().setText(null);
                            binding.transferBalanceInputText.getEditText().setText(null);
                        }
                    }
                }
            }
        }
        else if (v.equals(binding.balanceLoan25Btn))
            Utils.performCall(this, Uri.parse("tel:*234*3*1*25*1" + Uri.encode("#")));
        else if (v.equals(binding.balanceLoan50Btn))
            Utils.performCall(this, Uri.parse("tel:*234*3*1*50*1" + Uri.encode("#")));
        else if (v.equals(binding.balanceLoanCheckBtn))
            Utils.performCall(this, Uri.parse("tel:*222*233" + Uri.encode("#")));
        else if (v.equals(binding.privateCallCard) || v.equals(binding.revertedCallCard))
        {
            triggerView = v;
            launchContactPicker();
        }
        else if (v.equals(binding.transferPasswordChangeButton))
            new TransferPasswordChangeDialogFragment().show(getChildFragmentManager(), null);
    }

    private void launchContactPicker()
    {
        contactPickerResultLauncher.launch(new Intent(Intent.ACTION_PICK).setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        String voucherCode = binding.voucherCodeInputText.getEditText().getText().toString(),
                transferPhoneNumber = binding.transferPhoneNumberInputText.getEditText().getText().toString(),
                transferPassword = binding.transferPasswordInputText.getEditText().getText().toString(),
                transferBalance = binding.transferBalanceInputText.getEditText().getText().toString();
        if (triggerView != null)
            outState.putInt(TRIGGER_ID_SAVE_TAG, triggerView.getId());
        if (!voucherCode.isBlank())
            outState.putString(VOUCHER_CODE_SAVE_TAG, voucherCode);
        if (!transferPhoneNumber.isBlank())
            outState.putString(TRANSFER_PHONE_NUMBER_SAVE_TAG, transferPhoneNumber);
        if (!transferPassword.isBlank())
            outState.putString(TRANSFER_PASSWORD_SAVE_TAG, transferPassword);
        if (!transferBalance.isBlank())
            outState.putString(TRANSFER_BALANCE_VIEW_TAG, transferBalance);
        outState.putInt(SCROLL_POSITION_SAVE_TAG, binding.mainFragmentScrollview.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(TRIGGER_ID_SAVE_TAG))
                triggerView = requireActivity().findViewById(savedInstanceState.getInt(TRIGGER_ID_SAVE_TAG));
            if (savedInstanceState.containsKey(VOUCHER_CODE_SAVE_TAG))
                binding.voucherCodeInputText.getEditText().setText(savedInstanceState.getString(VOUCHER_CODE_SAVE_TAG, ""));
            if (savedInstanceState.containsKey(TRANSFER_PHONE_NUMBER_SAVE_TAG))
                binding.transferPhoneNumberInputText.getEditText().setText(savedInstanceState.getString(TRANSFER_PHONE_NUMBER_SAVE_TAG, ""));
            if (savedInstanceState.containsKey(TRANSFER_PASSWORD_SAVE_TAG))
                binding.transferPasswordInputText.getEditText().setText(savedInstanceState.getString(TRANSFER_PASSWORD_SAVE_TAG, ""));
            if (savedInstanceState.containsKey(TRANSFER_BALANCE_VIEW_TAG))
                binding.transferBalanceInputText.getEditText().setText(savedInstanceState.getString(TRANSFER_BALANCE_VIEW_TAG, ""));
            binding.mainFragmentScrollview.setScrollY(savedInstanceState.getInt(SCROLL_POSITION_SAVE_TAG));
        }
        super.onViewStateRestored(savedInstanceState);
    }
}