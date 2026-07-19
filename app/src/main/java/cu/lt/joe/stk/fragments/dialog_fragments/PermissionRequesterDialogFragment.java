package cu.lt.joe.stk.fragments.dialog_fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.stk.databinding.PermissionRequesterDialogLayoutBinding;
import cu.lt.joe.stk.utils.Utils;

public class PermissionRequesterDialogFragment extends DialogFragment
{
    private final static String DATA_URI_SAVE_TAG = "SAVED_URI", DATA_TERMINATOR_FLAG = "SHOULD_TERMINATE_APP";
    private Uri dataUri;
    private boolean shouldTerminateApp;
    protected final ActivityResultLauncher<String> callPermissionRequestLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted ->
    {
        if (granted && dataUri != null)
        {
            Utils.performCallFromFragment(this, dataUri);
            if (shouldTerminateApp) requireActivity().finish();
        }
        dismissAllowingStateLoss();
    });

    @NonNull
    public static PermissionRequesterDialogFragment newInstance(@NonNull Uri dataUri)
    {
        PermissionRequesterDialogFragment fragment = new PermissionRequesterDialogFragment();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putParcelable(DATA_URI_SAVE_TAG, dataUri);
        fragment.setArguments(bundleArgs);
        return fragment;
    }

    @NonNull
    public static PermissionRequesterDialogFragment newInstance(@NonNull Uri dataUri, boolean shouldTerminateApp)
    {
        PermissionRequesterDialogFragment fragment = new PermissionRequesterDialogFragment();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putParcelable(DATA_URI_SAVE_TAG, dataUri);
        bundleArgs.putBoolean(DATA_TERMINATOR_FLAG, shouldTerminateApp);
        fragment.setArguments(bundleArgs);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        if (args != null)
        {
            dataUri = args.getParcelable(DATA_URI_SAVE_TAG);
            shouldTerminateApp = args.containsKey(DATA_TERMINATOR_FLAG) && getArguments().getBoolean(DATA_TERMINATOR_FLAG);
        }
        PermissionRequesterDialogLayoutBinding binding = PermissionRequesterDialogLayoutBinding.inflate(getLayoutInflater());
        binding.permissionRequesterDialogOkButton.setOnClickListener(v -> callPermissionRequestLauncher.launch(Manifest.permission.CALL_PHONE));
        binding.permissionRequesterDialogCancelButton.setOnClickListener(v -> {
            if (shouldTerminateApp) requireActivity().finish();
            dismissAllowingStateLoss();
        });
        return new MaterialAlertDialogBuilder(requireActivity()).setView(binding.getRoot()).create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (shouldTerminateApp) requireActivity().finish();
    }
}