package cu.lt.joe.stk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import cu.lt.joe.stk.Constants;
import cu.lt.joe.stk.R;
import cu.lt.joe.stk.databinding.CrashLogItemLayoutBinding;
import cu.lt.joe.stk.objects.CrashLog;

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
        ((CrashLogsViewHolder) holder).bindCrashLog(crashLogsArrayList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return crashLogsArrayList.size();
    }

    private class CrashLogsViewHolder extends RecyclerView.ViewHolder
    {
        private final CrashLogItemLayoutBinding itemLayoutBinding;

        public CrashLogsViewHolder(CrashLogItemLayoutBinding itemLayoutBinding)
        {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        public void bindCrashLog(CrashLog crashLog)
        {
            itemLayoutBinding.setCrashLog(crashLog);
            itemLayoutBinding.crashLogDateTv.setText(new SimpleDateFormat(PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SAVED_DATE_FORMAT, context.getResources().getStringArray(R.array.date_formats)[0]), Locale.getDefault()).format(new Date(crashLog.getTimestamp())));
            itemLayoutBinding.executePendingBindings();
        }
    }
}