package cu.lt.joe.stk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.AdditionalOptionsItemLayoutBinding;
import cu.lt.joe.stk.objects.AdditionalOptionsMenuItem;
import cu.lt.joe.stk.utils.Utils;

public class AdditionalOptionsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final ArrayList<AdditionalOptionsMenuItem> items;

    public AdditionalOptionsItemAdapter(Context context, ArrayList<AdditionalOptionsMenuItem> items)
    {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new AdditionalOptionsItemViewHolder(
                AdditionalOptionsItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((AdditionalOptionsItemViewHolder) holder).bindAdditionalOptionMenuItem(position);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    private class AdditionalOptionsItemViewHolder extends RecyclerView.ViewHolder
    {
        private final AdditionalOptionsItemLayoutBinding itemViewBinding;

        public AdditionalOptionsItemViewHolder(AdditionalOptionsItemLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindAdditionalOptionMenuItem(int position)
        {
            AdditionalOptionsMenuItem additionalOptionsMenuItem = items.get(position);
            itemViewBinding.setAdditionalOptionsMenuItem(additionalOptionsMenuItem);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemViewBinding.getRoot().getLayoutParams());
            marginLayoutParams.topMargin = Utils.dpToPx(context, position > 0 ? 2 : 0);
            itemViewBinding.getRoot().setLayoutParams(marginLayoutParams);
            itemViewBinding.getRoot().setOnClickListener(v -> context.startActivity(additionalOptionsMenuItem.getTargetIntent()));
            itemViewBinding.executePendingBindings();
        }
    }
}