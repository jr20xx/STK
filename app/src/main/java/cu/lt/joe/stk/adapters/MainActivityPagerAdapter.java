package cu.lt.joe.stk.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainActivityPagerAdapter extends FragmentStateAdapter
{
    private final Fragment[] fragments;

    public MainActivityPagerAdapter(Fragment fragments[], @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return fragments[position];
    }

    @Override
    public int getItemCount()
    {
        return fragments.length;
    }
}