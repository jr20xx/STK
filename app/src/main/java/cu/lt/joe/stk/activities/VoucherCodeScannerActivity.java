package cu.lt.joe.stk.activities;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.VoucherCodeScannerLayoutBinding;

public class VoucherCodeScannerActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener
{
    private VoucherCodeScannerLayoutBinding binding;
    private CaptureManager captureManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.voucher_code_scanner_layout);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            binding.voucherScannerFlashlightControl.setVisibility(View.GONE);
        else
            binding.setTorchController(this);

        captureManager = new CaptureManager(this, binding.voucherCodeBarcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.setShowMissingCameraPermissionDialog(false);
        captureManager.decode();
    }

    @Override
    public void onTorchOn()
    {
        binding.voucherCodeBarcodeView.setTorchOn();
        binding.setTorchPowered(true);
    }

    @Override
    public void onTorchOff()
    {
        binding.voucherCodeBarcodeView.setTorchOff();
        binding.setTorchPowered(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return binding.voucherCodeBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}