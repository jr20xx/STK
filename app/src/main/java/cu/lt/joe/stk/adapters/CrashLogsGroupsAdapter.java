package cu.lt.joe.stk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.CrashLogsGroupLayoutBinding;
import cu.lt.joe.stk.objects.CrashLogsGroup;
import cu.lt.joe.stk.utils.Utils;

public class CrashLogsGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private ArrayList<CrashLogsGroup> crashLogsGroups;

    public CrashLogsGroupsAdapter(Context context, ArrayList<CrashLogsGroup> crashLogsGroups)
    {
        this.context = context;
        this.crashLogsGroups = crashLogsGroups;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CrashLogsGroupViewHolder(CrashLogsGroupLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((CrashLogsGroupViewHolder) holder).bindCrashLogsGroupAtIndex(position);
    }

    @Override
    public int getItemCount()
    {
        return crashLogsGroups.size();
    }

    private class CrashLogsGroupViewHolder extends RecyclerView.ViewHolder
    {
        private final CrashLogsGroupLayoutBinding itemViewBinding;

        public CrashLogsGroupViewHolder(CrashLogsGroupLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindCrashLogsGroupAtIndex(int index)
        {
            CrashLogsGroup crashLogsGroup = crashLogsGroups.get(index);
            itemViewBinding.crashLogsGroupHeaderTv.setText(crashLogsGroup.getFormattedDate());
            itemViewBinding.crashLogsGroupRecycler.setAdapter(new CrashLogsAdapter(context, crashLogsGroup.getCrashLogs()));

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemViewBinding.getRoot().getLayoutParams());
            marginLayoutParams.topMargin = Utils.dpToPx(context, index > 0 ? 12 : 8);
            marginLayoutParams.bottomMargin = Utils.dpToPx(context, index == crashLogsGroups.size() - 1 ? 12 : 0);
            itemViewBinding.getRoot().setLayoutParams(marginLayoutParams);

            itemViewBinding.executePendingBindings();
        }
    }
}