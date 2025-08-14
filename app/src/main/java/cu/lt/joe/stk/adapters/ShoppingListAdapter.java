package cu.lt.joe.stk.adapters;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import cu.lt.joe.stk.databinding.CarrierOffersGroupLayoutBinding;
import cu.lt.joe.stk.interfaces.OnCarrierOfferClickListener;
import cu.lt.joe.stk.objects.CarrierOffersGroup;
import cu.lt.joe.stk.utils.Utils;

public class ShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final FragmentActivity fragmentActivity;
    private final ArrayList<CarrierOffersGroup> carrierOffersGroups;
    private final OnCarrierOfferClickListener onCarrierOfferClickListener;

    public ShoppingListAdapter(FragmentActivity fragmentActivity, ArrayList<CarrierOffersGroup> carrierOffersGroups, OnCarrierOfferClickListener onCarrierOfferClickListener)
    {
        this.fragmentActivity = fragmentActivity;
        this.carrierOffersGroups = carrierOffersGroups;
        this.onCarrierOfferClickListener = onCarrierOfferClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(fragmentActivity);
        return new CarrierOfferGroupViewHolder(CarrierOffersGroupLayoutBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((CarrierOfferGroupViewHolder) holder).bindCarrierOffersGroup(carrierOffersGroups.get(position));
    }

    @Override
    public int getItemCount()
    {
        return carrierOffersGroups.size();
    }

    private class CarrierOfferGroupViewHolder extends RecyclerView.ViewHolder
    {
        private final CarrierOffersGroupLayoutBinding itemViewBinding;

        public CarrierOfferGroupViewHolder(@NonNull CarrierOffersGroupLayoutBinding itemViewBinding)
        {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
            this.itemViewBinding.carrierOffersRecycler.setNestedScrollingEnabled(false);
        }

        public void bindCarrierOffersGroup(CarrierOffersGroup carrierOffersGroup)
        {
            itemViewBinding.setCarrierOfferGroup(carrierOffersGroup);
            itemViewBinding.carrierOffersRecycler.setAdapter(new ShoppingListSubItemsAdapter(fragmentActivity, carrierOffersGroup.getCarrierOffers(), onCarrierOfferClickListener));
            DisplayMetrics dm = new DisplayMetrics();
            fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int spanCount = dm.widthPixels / Utils.dpToPx(fragmentActivity, 180);
            itemViewBinding.carrierOffersRecycler.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
            itemViewBinding.executePendingBindings();
        }
    }
}