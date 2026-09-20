package cu.lt.joe.stk.adapters.crash_log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.CrashLogItemOptionLayoutBinding;
import cu.lt.joe.stk.objects.crash_log.CrashLogItemOption;
import cu.lt.joe.stk.utils.Utils;

public class CrashLogItemOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final ArrayList<CrashLogItemOption> crashLogsOptionsArrayList;

    public CrashLogItemOptionAdapter(Context context, ArrayList<CrashLogItemOption> crashLogsOptionsArrayList)
    {
        this.context = context;
        this.crashLogsOptionsArrayList = crashLogsOptionsArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CrashLogOptionItemViewHolder(CrashLogItemOptionLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((CrashLogOptionItemViewHolder) holder).bindCrashLogOptionAtIndex(position);
    }

    @Override
    public int getItemCount()
    {
        return crashLogsOptionsArrayList.size();
    }

    private class CrashLogOptionItemViewHolder extends RecyclerView.ViewHolder
    {
        private final CrashLogItemOptionLayoutBinding itemViewBinding;

        public CrashLogOptionItemViewHolder(CrashLogItemOptionLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindCrashLogOptionAtIndex(int index)
        {
            CrashLogItemOption crashLogItemOption = crashLogsOptionsArrayList.get(index);
            itemViewBinding.setCrashLogItemOption(crashLogItemOption);

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemViewBinding.getRoot().getLayoutParams());
            marginLayoutParams.topMargin = Utils.dpToPx(context, index > 0 ? 2 : 0);
            itemViewBinding.getRoot().setLayoutParams(marginLayoutParams);

            itemViewBinding.executePendingBindings();
        }
    }
}