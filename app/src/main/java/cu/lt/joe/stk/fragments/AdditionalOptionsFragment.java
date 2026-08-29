package cu.lt.joe.stk.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.activities.SecondaryActivity;
import cu.lt.joe.stk.adapters.AdditionalOptionsItemAdapter;
import cu.lt.joe.stk.databinding.AdditionalOptionsLayoutBinding;
import cu.lt.joe.stk.objects.AdditionalOptionsMenuItem;

public class AdditionalOptionsFragment extends Fragment
{
    private AdditionalOptionsLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = AdditionalOptionsLayoutBinding.inflate(inflater, container, false);

        ArrayList<AdditionalOptionsMenuItem> additionalItems = new ArrayList<>();
        additionalItems.add(new AdditionalOptionsMenuItem(R.drawable.ic_settings_outline,
                getString(R.string.settings_fragment_title),
                new Intent(requireActivity(), SecondaryActivity.class).putExtra(Intent.EXTRA_TITLE, R.string.settings_fragment_title)));
        additionalItems.add(new AdditionalOptionsMenuItem(R.drawable.ic_crash_log,
                getString(R.string.crash_logs_fragment_title),
                new Intent(requireActivity(), SecondaryActivity.class).putExtra(Intent.EXTRA_TITLE, R.string.crash_logs_fragment_title)));
        binding.additionalOptionsMenuRecycler.setAdapter(new AdditionalOptionsItemAdapter(requireContext(), additionalItems));

        ArrayList<AdditionalOptionsMenuItem> additionalLinks = new ArrayList<>();
        additionalLinks.add(new AdditionalOptionsMenuItem(R.drawable.ic_help,
                getString(R.string.balance_recharge_faq_header),
                new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://etecsa.cu/es/preguntas-frecuentes?faq=374"))));
        additionalLinks.add(new AdditionalOptionsMenuItem(R.drawable.ic_help,
                getString(R.string.mobile_plans_faq_header),
                new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://etecsa.cu/es/preguntas-frecuentes?faq=4781"))));
        binding.additionalInfoRecycler.setAdapter(new AdditionalOptionsItemAdapter(requireContext(), additionalLinks));

        binding.setContainerFragment(this);
        return binding.getRoot();
    }

    public void openExternalLink(String webLink)
    {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webLink)));
        }
        catch (Exception e)
        {
            Snackbar.make(binding.getRoot(), R.string.opening_link_error_tip, Snackbar.LENGTH_SHORT).show();
        }
    }
}