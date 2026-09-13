package cu.lt.joe.stk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.CrashLogItemLayoutBinding;
import cu.lt.joe.stk.objects.CrashLog;
import cu.lt.joe.stk.utils.Utils;

public class CrashLogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final ArrayList<CrashLog> crashLogsArrayList;

    public CrashLogsAdapter(Context context, ArrayList<CrashLog> crashLogsArrayList)
    {
        this.context = context;
        this.crashLogsArrayList = crashLogsArrayList;
    }

    public void removeCrashLog(int index)
    {
        crashLogsArrayList.remove(index);
        notifyItemRemoved(index);
    }

    public void removeCrashLog(CrashLog crashLog)
    {
        int position = crashLogsArrayList.indexOf(crashLog);
        crashLogsArrayList.remove(crashLog);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CrashLogsViewHolder(CrashLogItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((CrashLogsViewHolder) holder).bindCrashLogAtIndex(position);
    }

    @Override
    public int getItemCount()
    {
        return crashLogsArrayList.size();
    }

    private class CrashLogsViewHolder extends RecyclerView.ViewHolder
    {
        private final CrashLogItemLayoutBinding itemViewBinding;

        public CrashLogsViewHolder(CrashLogItemLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindCrashLogAtIndex(int index)
        {
            CrashLog crashLog = crashLogsArrayList.get(index);
            itemViewBinding.setCrashLog(crashLog);

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemViewBinding.getRoot().getLayoutParams());
            marginLayoutParams.topMargin = Utils.dpToPx(context, index > 0 ? 2 : 0);
            itemViewBinding.getRoot().setLayoutParams(marginLayoutParams);

            itemViewBinding.getRoot().setOnClickListener(v -> {
                TransitionManager.beginDelayedTransition(
                        (ViewGroup) v.getRootView(),
                        new TransitionSet()
                                .addTransition(new ChangeBounds())
                                .addTransition(new Fade(crashLog.isSelected().get() ? Fade.OUT : Fade.IN).setStartDelay(100))
                                .setDuration(220)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                );
                crashLog.setSelected(!crashLog.isSelected().get());
            });

            itemViewBinding.executePendingBindings();
        }
    }
}