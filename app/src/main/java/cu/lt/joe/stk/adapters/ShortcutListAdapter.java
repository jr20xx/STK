package cu.lt.joe.stk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.ShortcutSelectorItemLayoutBinding;
import cu.lt.joe.stk.interfaces.OnShortcutItemSelectedListener;
import cu.lt.joe.stk.objects.ShortcutItem;

public class ShortcutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final ArrayList<ShortcutItem> shortcutItems;
    private final OnShortcutItemSelectedListener onShortcutItemSelectedListener;

    public ShortcutListAdapter(Context context, ArrayList<ShortcutItem> shortcutItems, OnShortcutItemSelectedListener onShortcutItemSelectedListener)
    {
        this.context = context;
        this.shortcutItems = shortcutItems;
        this.onShortcutItemSelectedListener = onShortcutItemSelectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ShortcutItemViewHolder(
                ShortcutSelectorItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((ShortcutItemViewHolder) holder).bindShortcut(shortcutItems.get(position));
    }

    @Override
    public int getItemCount()
    {
        return shortcutItems.size();
    }

    private class ShortcutItemViewHolder extends RecyclerView.ViewHolder
    {
        private final ShortcutSelectorItemLayoutBinding itemViewBinding;

        public ShortcutItemViewHolder(ShortcutSelectorItemLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindShortcut(ShortcutItem shortcutItem)
        {
            itemViewBinding.setShortcutItem(shortcutItem);
            itemViewBinding.getRoot().setOnClickListener(v -> onShortcutItemSelectedListener.onShortcutItemSelected(shortcutItem));
            itemViewBinding.executePendingBindings();
        }
    }
}