package cu.lt.joe.stk.fragments.dialog_fragments;

import android.Manifest;
import android.app.Dialog;
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
    private final static String DATA_URI_SAVE_TAG = "SAVED_URI";
    private Uri dataUri;
    protected final ActivityResultLauncher<String> callPermissionRequestLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted ->
    {
        if (granted && dataUri != null)
            Utils.performCall(this, dataUri);
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
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        if (getArguments() != null)
            dataUri = getArguments().getParcelable(DATA_URI_SAVE_TAG);
        PermissionRequesterDialogLayoutBinding binding = PermissionRequesterDialogLayoutBinding.inflate(getLayoutInflater());
        binding.permissionRequesterDialogOkButton.setOnClickListener(v -> callPermissionRequestLauncher.launch(Manifest.permission.CALL_PHONE));
        binding.permissionRequesterDialogCancelButton.setOnClickListener(v -> dismissAllowingStateLoss());
        return new MaterialAlertDialogBuilder(requireActivity())
                .setView(binding.getRoot())
                .create();
    }
}