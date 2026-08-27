package cu.lt.joe.stk.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.adapters.MainActivityPagerAdapter;
import cu.lt.joe.stk.databinding.MainLayoutBinding;
import cu.lt.joe.stk.fragments.AdditionalOptionsFragment;
import cu.lt.joe.stk.fragments.MainFragment;
import cu.lt.joe.stk.fragments.ShoppingFragment;

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
                new Fragment[]{new MainFragment(), new ShoppingFragment(), new AdditionalOptionsFragment()},
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
            else if (item.getTitle().toString().equals(getString(R.string.more_options_fragment_menu_title)))
            {
                binding.mainActivityPager.setCurrentItem(2);
                binding.mainActivityTitleTv.setText(R.string.more_options_fragment_menu_title);
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
                        binding.bottomNavigationView.setSelectedItemId(R.id.more_options_im);
                        break;
                    default:
                        super.onPageSelected(position);
                }
            }
        });
        setSupportActionBar(binding.mainActivityToolbar);
        binding.mainActivityPager.setOffscreenPageLimit(2);
    }
}