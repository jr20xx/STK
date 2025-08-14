package cu.lt.joe.stk.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.adapters.MainActivityPagerAdapter;
import cu.lt.joe.stk.databinding.MainLayoutBinding;
import cu.lt.joe.stk.fragments.InformationFragment;
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
                new Fragment[]{new MainFragment(), new ShoppingFragment(), new InformationFragment()},
                getSupportFragmentManager(), getLifecycle()
        ));
        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            switch (item.getTitle().toString())
            {
                case "Inicio":
                    binding.mainActivityPager.setCurrentItem(0);
                    binding.mainActivityTitleTv.setText(R.string.app_name);
                    return true;
                case "Compras":
                    binding.mainActivityPager.setCurrentItem(1);
                    binding.mainActivityTitleTv.setText("Planes y paquetes");
                    return true;
                case "Info":
                    binding.mainActivityPager.setCurrentItem(2);
                    binding.mainActivityTitleTv.setText("Información");
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
                        binding.bottomNavigationView.setSelectedItemId(R.id.info_fragment_im);
                        break;
                    default:
                        super.onPageSelected(position);
                }
            }
        });
        setSupportActionBar(binding.mainActivityToolbar);
        binding.mainActivityPager.setOffscreenPageLimit(2);
        binding.themeModeToggleButton.setOnClickListener(v ->
        {
            sharedPreferences.edit().putBoolean(THEME_MODE_SAVER_PREFERENCE_KEY,
                    !sharedPreferences.getBoolean(THEME_MODE_SAVER_PREFERENCE_KEY, false)).apply();
            recreate();
        });
    }
}