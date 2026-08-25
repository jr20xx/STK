package cu.lt.joe.stk.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.adapters.AdditionalOptionsMenuItemAdapter;
import cu.lt.joe.stk.adapters.MainActivityPagerAdapter;
import cu.lt.joe.stk.databinding.AdditionalOptionsMenuLayoutBinding;
import cu.lt.joe.stk.databinding.MainLayoutBinding;
import cu.lt.joe.stk.fragments.MainFragment;
import cu.lt.joe.stk.fragments.SettingsFragment;
import cu.lt.joe.stk.fragments.ShoppingFragment;
import cu.lt.joe.stk.objects.AdditionalOptionsMenuItem;

public class MainActivity extends BaseActivity
{
    private MainLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_layout);
        binding.mainActivityPager.setSaveEnabled(true);
        binding.mainActivityPager.setAdapter(new MainActivityPagerAdapter(
                new Fragment[]{new MainFragment(), new ShoppingFragment(), new SettingsFragment()},
                getSupportFragmentManager(), getLifecycle()
        ));
        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            if (item.getTitle().toString().equals(getString(R.string.home_fragment_menu_title)))
            {
                binding.mainActivityPager.setCurrentItem(0);
                binding.mainActivityTitleTv.setText(R.string.home_fragment_menu_title);
                return true;
            }
            else if (item.getTitle().toString().equals(getString(R.string.shop_fragment_menu_title)))
            {
                binding.mainActivityPager.setCurrentItem(1);
                binding.mainActivityTitleTv.setText(R.string.shop_fragment_title);
                return true;
            }
            else if (item.getTitle().toString().equals(getString(R.string.settings_fragment_menu_title)))
            {
                binding.mainActivityPager.setCurrentItem(2);
                binding.mainActivityTitleTv.setText(R.string.settings_fragment_menu_title);
                return true;
            }
            return false;
        });
        binding.mainActivityPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        binding.bottomNavigationView.setSelectedItemId(R.id.main_fragment_im);
                        break;
                    case 1:
                        binding.bottomNavigationView.setSelectedItemId(R.id.shopping_fragment_im);
                        break;
                    case 2:
                        binding.bottomNavigationView.setSelectedItemId(R.id.settings_fragment_im);
                        break;
                    default:
                        super.onPageSelected(position);
                }
            }
        });
        setSupportActionBar(binding.mainActivityToolbar);
        binding.mainActivityPager.setOffscreenPageLimit(2);
        binding.additionalOptionsMenuOpenerButton.setOnClickListener(v ->
        {
            //TODO: Write the code to open the menu with additional stuffs
            BottomSheetDialog additionalOptionsMenu = new BottomSheetDialog(this);
            AdditionalOptionsMenuLayoutBinding menuLayoutBinding = AdditionalOptionsMenuLayoutBinding.inflate(getLayoutInflater());
            ArrayList<AdditionalOptionsMenuItem> additionalOptionsMenuItems = new ArrayList<>();
            additionalOptionsMenuItems.add(new AdditionalOptionsMenuItem(R.drawable.ic_crash_log, "Registro de  fallos", new Intent(this, CrashLogsViewerActivity.class)));
            menuLayoutBinding.additionalOptionsMenuRecycler.setAdapter(new AdditionalOptionsMenuItemAdapter(this, additionalOptionsMenuItems));
            additionalOptionsMenu.setContentView(menuLayoutBinding.getRoot());
            additionalOptionsMenu.show();
        });
    }
}