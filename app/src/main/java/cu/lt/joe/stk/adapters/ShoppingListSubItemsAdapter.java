package cu.lt.joe.stk.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.BasicOfferCardLayoutBinding;
import cu.lt.joe.stk.databinding.DataPacketCardLayoutBinding;
import cu.lt.joe.stk.interfaces.OnCarrierOfferClickListener;
import cu.lt.joe.stk.objects.BasicOffer;
import cu.lt.joe.stk.objects.DataPacket;

public class ShoppingListSubItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final FragmentActivity fragmentActivity;
    private final ArrayList items;
    private final OnCarrierOfferClickListener onCarrierOfferClickListener;

    public ShoppingListSubItemsAdapter(FragmentActivity fragmentActivity, ArrayList items, OnCarrierOfferClickListener onCarrierOfferClickListener)
    {
        this.fragmentActivity = fragmentActivity;
        this.items = items;
        this.onCarrierOfferClickListener = onCarrierOfferClickListener;
    }

    @Override
    public int getItemViewType(int position)
    {
        Object item = items.get(position);
        if (item instanceof DataPacket)
            return 1;
        else if (item instanceof BasicOffer)
            return 2;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(fragmentActivity);
        if (viewType == 1)
            return new DataPacketViewHolder(DataPacketCardLayoutBinding.inflate(inflater, parent, false));
        //else if (viewType == 2)
        return new BasicOfferViewHolder(BasicOfferCardLayoutBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof DataPacketViewHolder)
            ((DataPacketViewHolder) holder).bindDataPacket((DataPacket) items.get(position));
        else if (holder instanceof BasicOfferViewHolder)
            ((BasicOfferViewHolder) holder).bindBasicOffer((BasicOffer) items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    private class DataPacketViewHolder extends RecyclerView.ViewHolder
    {
        private final DataPacketCardLayoutBinding itemViewBinding;

        public DataPacketViewHolder(@NonNull DataPacketCardLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindDataPacket(DataPacket dataPacket)
        {
            itemViewBinding.setDataPacket(dataPacket);
            itemViewBinding.getRoot().setOnClickListener(v -> onCarrierOfferClickListener.onCarrierOfferClick(dataPacket.getDialingUri()));
            itemViewBinding.executePendingBindings();
        }
    }

    private class BasicOfferViewHolder extends RecyclerView.ViewHolder
    {
        private final BasicOfferCardLayoutBinding itemViewBinding;

        public BasicOfferViewHolder(@NonNull BasicOfferCardLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }

        public void bindBasicOffer(BasicOffer basicOffer)
        {
            itemViewBinding.setBasicOffer(basicOffer);
            itemViewBinding.getRoot().setOnClickListener(v -> onCarrierOfferClickListener.onCarrierOfferClick(basicOffer.getDialingUri()));
            itemViewBinding.executePendingBindings();
        }
    }
}