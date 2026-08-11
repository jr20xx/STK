package cu.lt.joe.stk.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.ArrayList;
import java.util.Objects;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.activities.VoucherCodeScannerActivity;
import cu.lt.joe.stk.adapters.ConsultItemAdapter;
import cu.lt.joe.stk.databinding.MainFragmentBinding;
import cu.lt.joe.stk.fragments.dialog_fragments.TransferCentsWarningDialogFragment;
import cu.lt.joe.stk.fragments.dialog_fragments.TransferPasswordChangeDialogFragment;
import cu.lt.joe.stk.objects.ConsultItem;
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
                        Snackbar.make(binding.getRoot(), R.string.invalid_code_tip, Snackbar.LENGTH_SHORT).show();
                    else
                    {
                        obtainedContents = obtainedContents.trim();
                        for (char c : obtainedContents.toCharArray())
                            if (c < '0' || c > '9')
                            {
                                Snackbar.make(binding.getRoot(), R.string.malformed_code_tip, Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        binding.voucherCodeInputText.getEditText().setText(obtainedContents);
                    }
                }
            });
    private SharedPreferences sharp;
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
                                Snackbar.make(binding.getRoot(), R.string.invalid_phone_number_tip, Snackbar.LENGTH_SHORT).show();
                            else
                            {
                                if (triggerView != null)
                                {
                                    if (triggerView.equals(binding.privateCallCard))
                                        Utils.performCallFromFragment(this, Uri.parse("tel:" + Uri.encode("#") + "31" + Uri.encode("#") + numberAsString));
                                    else if (triggerView.equals(binding.revertedCallCard))
                                        Utils.performCallFromFragment(this, Uri.parse("tel:" +
                                                Constants.REVERTED_CALL_PREFIXES_ARRAY[sharp.getInt(Constants.REVERTED_CALL_PREFIX_INDEX, 0)] +
                                                numberAsString));
                                    else
                                        binding.transferPhoneNumberInputText.getEditText().setText(numberAsString);
                                }
                            }
                        }
                    }
                    catch (Exception ignored)
                    {
                        Snackbar.make(binding.getRoot(), R.string.unusable_phone_number_tip, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sharp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        binding = MainFragmentBinding.inflate(inflater, container, false);
        binding.setUSSDRequester(this);

        ArrayList<ConsultItem> consultItems = new ArrayList<>();
        consultItems.add(new ConsultItem(R.drawable.ic_balance, getString(R.string.balance_label), sharp.getString(Constants.LAST_KNOWN_BALANCE, null), Uri.fromParts("tel", getString(R.string.balance_check_ussd_code), null)));
        consultItems.add(new ConsultItem(R.drawable.ic_data, getString(R.string.data_label), sharp.getString(Constants.LAST_KNOWN_INTERNET_DATA, null), Uri.fromParts("tel", getString(R.string.data_check_ussd_code), null)));
        consultItems.add(new ConsultItem(R.drawable.ic_message, getString(R.string.messages_label), sharp.getString(Constants.LAST_KNOWN_MESSAGES_COUNT, null), Uri.fromParts("tel", getString(R.string.messages_check_ussd_code), null)));
        consultItems.add(new ConsultItem(R.drawable.ic_call, getString(R.string.minutes_label), sharp.getString(Constants.LAST_KNOWN_MINUTES_COUNT, sharp.getString(Constants.LAST_KNOWN_MINUTES_COUNT, null)), Uri.fromParts("tel", getString(R.string.minutes_check_ussd_code), null)));
        consultItems.add(new ConsultItem(R.drawable.ic_bonus, getString(R.string.bonuses_label), sharp.getString(Constants.LAST_KNOWN_BONUSES, null), Uri.fromParts("tel", getString(R.string.bonuses_check_ussd_code), null)));
        consultItems.add(new ConsultItem(R.drawable.ic_no_recharge, getString(R.string.recharge_label), sharp.getString(Constants.LAST_KNOWN_RECHARGE_TIME, null), Uri.fromParts("tel", getString(R.string.recharge_check_ussd_code), null)));
        ConsultItemAdapter consultItemsAdapter = new ConsultItemAdapter(this, consultItems);
        binding.consultItemsList.setNestedScrollingEnabled(false);
        binding.consultItemsList.setAdapter(consultItemsAdapter);

        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int spanCount = dm.widthPixels / Utils.dpToPx(requireContext(), 180);
        binding.consultItemsList.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));

        sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key != null && !(key.equals(Constants.IS_DARK_MODE_ENABLED) || key.equals(Constants.REVERTED_CALL_PREFIX_INDEX)))
            {
                String newValue = sharp.getString(key, null);
                switch (key)
                {
                    case Constants.LAST_KNOWN_BALANCE:
                        consultItemsAdapter.updateAvailableInfoAtPosition(0, newValue);
                        break;
                    case Constants.LAST_KNOWN_INTERNET_DATA:
                        consultItemsAdapter.updateAvailableInfoAtPosition(1, newValue);
                        break;
                    case Constants.LAST_KNOWN_MESSAGES_COUNT:
                        consultItemsAdapter.updateAvailableInfoAtPosition(2, newValue);
                        break;
                    case Constants.LAST_KNOWN_MINUTES_COUNT:
                        consultItemsAdapter.updateAvailableInfoAtPosition(3, newValue);
                        break;
                }
            }
        };
        sharp.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

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

        getChildFragmentManager().setFragmentResultListener(
                TransferCentsWarningDialogFragment.DIALOG_REQUEST_KEY,
                this, (requestKey, bundle) -> {
                    if (requestKey.equals(TransferCentsWarningDialogFragment.DIALOG_REQUEST_KEY))
                    {
                        if (bundle.getBoolean(TransferCentsWarningDialogFragment.RESULT_KEY, false))
                        {
                            Utils.performCallFromFragment(this, Uri.parse("tel:*234*1*" + binding.transferPhoneNumberInputText.getEditText().getText().toString() + "*" + binding.transferPasswordInputText.getEditText().getText().toString() + Uri.encode("#")));
                            cleanTransferFields();
                        }
                    }
                }
        );
        return binding.getRoot();
    }

    public void executeUSSDRequest(View v)
    {
        if (v.equals(binding.rechargeButton))
        {
            String voucherActivationCode = binding.voucherCodeInputText.getEditText().getText().toString();
            binding.voucherCodeInputText.setErrorEnabled(true);
            if (voucherActivationCode.isBlank() || voucherActivationCode.length() < 16)
                binding.voucherCodeInputText.setError(getString(R.string.recharge_code_length_error));
            else
            {
                binding.voucherCodeInputText.setError(null);
                binding.voucherCodeInputText.setErrorEnabled(false);
                Utils.performCallFromFragment(this, Uri.parse("tel:*662*" + voucherActivationCode + Uri.encode("#")));
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
                binding.transferPhoneNumberInputText.setError(getString(R.string.transfer_receiver_phone_number_length_error));
            else if (receiverNumber.charAt(0) != '5' && receiverNumber.charAt(0) != '6')
                binding.transferPhoneNumberInputText.setError(getString(R.string.transfer_receiver_phone_number_starting_error));
            else
            {
                binding.transferPhoneNumberInputText.setError(null);
                binding.transferPhoneNumberInputText.setErrorEnabled(false);
                binding.transferPasswordInputText.setErrorEnabled(true);
                if (passwordCode.isBlank() || passwordCode.length() < 4)
                    binding.transferPasswordInputText.setError(getString(R.string.transfer_key_length_error));
                else
                {
                    binding.transferPasswordInputText.setError(null);
                    binding.transferPasswordInputText.setErrorEnabled(false);
                    binding.transferBalanceInputText.setErrorEnabled(true);
                    if (balanceAmount.isBlank())
                        binding.transferBalanceInputText.setError(getString(R.string.transfer_balance_empty_error));
                    else if (Double.parseDouble(balanceAmount) == 0)
                        binding.transferBalanceInputText.setError(getString(R.string.transfer_balance_is_zero_error));
                    else
                    {
                        binding.transferBalanceInputText.setError(null);
                        binding.transferBalanceInputText.setErrorEnabled(false);
                        if (balanceAmount.contains(".") || balanceAmount.contains(","))
                            new TransferCentsWarningDialogFragment().show(getChildFragmentManager(), null);
                        else
                        {
                            Utils.performCallFromFragment(this, Uri.parse("tel:*234*1*" + receiverNumber + "*" + passwordCode + "*" + balanceAmount + Uri.encode("#")));
                            cleanTransferFields();
                        }
                    }
                }
            }
        }
        else if (v.equals(binding.balanceLoan25Btn))
            Utils.performCallFromFragment(this, Uri.parse("tel:*234*3*1*25*1" + Uri.encode("#")));
        else if (v.equals(binding.balanceLoan50Btn))
            Utils.performCallFromFragment(this, Uri.parse("tel:*234*3*1*50*1" + Uri.encode("#")));
        else if (v.equals(binding.balanceLoanCheckBtn))
            Utils.performCallFromFragment(this, Uri.parse("tel:*222*233" + Uri.encode("#")));
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

    private void cleanTransferFields()
    {
        binding.transferPhoneNumberInputText.getEditText().setText(null);
        binding.transferPasswordInputText.getEditText().setText(null);
        binding.transferBalanceInputText.getEditText().setText(null);
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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (sharp != null && sharedPreferenceChangeListener != null)
            sharp.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }
}