package cu.lt.joe.stk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import cu.lt.joe.stk.R;
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
        additionalItems.add(new AdditionalOptionsMenuItem(R.drawable.ic_settings_outline, getString(R.string.settings_fragment_menu_title), null));
        additionalItems.add(new AdditionalOptionsMenuItem(R.drawable.ic_crash_log, "Registro de errores", null));
        binding.additionalOptionsMenuRecycler.setAdapter(new AdditionalOptionsItemAdapter(requireContext(), additionalItems));

        return binding.getRoot();
    }
}