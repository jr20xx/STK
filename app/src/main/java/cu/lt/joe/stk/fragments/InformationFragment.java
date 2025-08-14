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
import cu.lt.joe.stk.databinding.InfoFragmentBinding;

public class InformationFragment extends Fragment
{
    private static final String SCROLL_POSITION_SAVER_TAG = "SAVED_SCROLL_POSITION";
    private InfoFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = InfoFragmentBinding.inflate(inflater, container, false);
        binding.setLinkOpener(this);
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
            Snackbar.make(binding.getRoot(), "No se puede abrir el enlace", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(SCROLL_POSITION_SAVER_TAG, binding.infoFragmentScrollview.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
            binding.infoFragmentScrollview.setScrollY(savedInstanceState.getInt(SCROLL_POSITION_SAVER_TAG));
        super.onViewStateRestored(savedInstanceState);
    }
}