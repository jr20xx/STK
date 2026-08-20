package cu.lt.joe.stk.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.adapters.ShortcutListAdapter;
import cu.lt.joe.stk.databinding.ShortcutSelectorLayoutBinding;
import cu.lt.joe.stk.fragments.dialog_fragments.PermissionRequesterDialogFragment;
import cu.lt.joe.stk.objects.ShortcutItem;
import cu.lt.joe.stk.utils.ShortcutUtils;

public class ShortcutActivity extends BaseActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction()))
        {
            setTheme(R.style.Theme_STK);
            super.applyDynamicColors();
            ShortcutSelectorLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.shortcut_selector_layout);
            ArrayList<ShortcutItem> shortcutItems = new ArrayList<>();
            shortcutItems.add(new ShortcutItem(R.drawable.ic_balance, getString(R.string.balance_label), getString(R.string.balance_check_label), getString(R.string.balance_check_ussd_code)));
            shortcutItems.add(new ShortcutItem(R.drawable.ic_data, getString(R.string.data_label), getString(R.string.data_check_label), getString(R.string.data_check_ussd_code)));
            shortcutItems.add(new ShortcutItem(R.drawable.ic_message, getString(R.string.messages_label), getString(R.string.messages_check_label), getString(R.string.messages_check_ussd_code)));
            shortcutItems.add(new ShortcutItem(R.drawable.ic_call, getString(R.string.minutes_label), getString(R.string.minutes_check_label), getString(R.string.minutes_check_ussd_code)));
            shortcutItems.add(new ShortcutItem(R.drawable.ic_bonus, getString(R.string.bonuses_label), getString(R.string.bonuses_check_label), getString(R.string.bonuses_check_ussd_code)));
            shortcutItems.add(new ShortcutItem(R.drawable.ic_no_recharge, getString(R.string.recharge_label), getString(R.string.recharge_check_label), getString(R.string.recharge_check_ussd_code)));
            binding.shortcutListRv.setAdapter(new ShortcutListAdapter
                    (this, shortcutItems, (shortcutItem) -> {
                        Bitmap iconBitmap = ShortcutUtils.getShortcutIconBitmap(ShortcutActivity.this, Color.rgb(204, 12, 12), shortcutItem.getIconResId());
                        String dialingNumber = shortcutItem.getDialingNumber();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            ShortcutManager sm = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);
                            IconCompat icon = IconCompat.createWithAdaptiveBitmap(iconBitmap);
                            String id = "dial_shortcut_" + System.currentTimeMillis();
                            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(ShortcutActivity.this, id)
                                    .setShortLabel(shortcutItem.getShortTitle())
                                    .setLongLabel(shortcutItem.getShortTitle())
                                    .setIntent(ShortcutUtils.getTargetIntent(ShortcutActivity.this, dialingNumber))
                                    .setIcon(icon.toIcon(ShortcutActivity.this))
                                    .build();
                            setResult(ShortcutActivity.RESULT_OK, sm.createShortcutResultIntent(shortcutInfo));
                        }
                        else
                        {
                            setResult(ShortcutActivity.RESULT_OK, new Intent()
                                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, ShortcutUtils.getTargetIntent(ShortcutActivity.this, dialingNumber))
                                    .putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutItem.getShortTitle())
                                    .putExtra(Intent.EXTRA_SHORTCUT_ICON, iconBitmap));
                        }
                        finish();
                    })
            );
            binding.shortcutListRv.setLayoutManager(new LinearLayoutManager(this));
        }
        else if (Constants.SHORTCUT_ACTION_DIAL.equals(getIntent().getAction()) && getIntent().hasExtra(Constants.SHORTCUT_USSD_CODE))
        {
            try
            {
                Uri uriToDial = Uri.fromParts("tel", getIntent().getStringExtra(Constants.SHORTCUT_USSD_CODE), null);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(new Intent(Intent.ACTION_CALL, uriToDial));
                    finish();
                }
                else
                    PermissionRequesterDialogFragment.newInstance(uriToDial, true).show(getSupportFragmentManager(), null);
            }
            catch (Exception e)
            {
                Toast.makeText(this, R.string.dialing_error_tip, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else finish();
    }
}