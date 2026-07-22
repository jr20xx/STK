package cu.lt.joe.stk.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.ConsultItemLayoutBinding;
import cu.lt.joe.stk.objects.ConsultItem;
import cu.lt.joe.stk.utils.Utils;

public class ConsultItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final ArrayList<ConsultItem> consultItems;
    private final Fragment parentFragment;

    public ConsultItemAdapter(Fragment parentFragment, ArrayList<ConsultItem> consultItems)
    {
        this.parentFragment = parentFragment;
        this.consultItems = consultItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ConsultItemViewHolder(ConsultItemLayoutBinding.inflate(LayoutInflater.from(parentFragment.requireContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((ConsultItemAdapter.ConsultItemViewHolder) holder).bindConsultItem(consultItems.get(position));
    }

    @Override
    public int getItemCount()
    {
        return consultItems.size();
    }

    public void updateAvailableInfoAtPosition(int position, String newInfo)
    {
        consultItems.get(position).setAvailableInfo(newInfo);
        notifyItemChanged(position);
    }

    private class ConsultItemViewHolder extends RecyclerView.ViewHolder
    {
        private final ConsultItemLayoutBinding itemViewBinding;

        public ConsultItemViewHolder(@NonNull ConsultItemLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindConsultItem(ConsultItem consultItem)
        {
            itemViewBinding.setConsultItem(consultItem);
            itemViewBinding.getRoot().setOnClickListener(v -> Utils.performCallFromFragment(parentFragment, consultItem.getUriToDial()));
            itemViewBinding.executePendingBindings();
        }
    }
}