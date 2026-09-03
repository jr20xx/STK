package cu.lt.joe.stk.fragments.additional_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cu.lt.joe.stk.databinding.ChangeSimCardPinLayoutBinding;

public class ChangeSIMCardPinFragment extends Fragment
{
    private ChangeSimCardPinLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = ChangeSimCardPinLayoutBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}