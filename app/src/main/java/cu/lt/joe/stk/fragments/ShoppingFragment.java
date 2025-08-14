package cu.lt.joe.stk.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import cu.lt.joe.stk.adapters.ShoppingListAdapter;
import cu.lt.joe.stk.databinding.ShoppingFragmentLayoutBinding;
import cu.lt.joe.stk.objects.BasicOffer;
import cu.lt.joe.stk.objects.CarrierOffersGroup;
import cu.lt.joe.stk.objects.DataPacket;
import cu.lt.joe.stk.utils.Utils;

public class ShoppingFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ShoppingFragmentLayoutBinding binding = ShoppingFragmentLayoutBinding.inflate(inflater, container, false);
        ArrayList<CarrierOffersGroup> carrierOffersGroups = new ArrayList<>();

        ArrayList<DataPacket> dataPackets = new ArrayList<>();
        dataPackets.add(new DataPacket("4,5 GB", null, null, "240 CUP", Uri.parse("tel:*133*1*4*1" + Uri.encode("#"))));
        dataPackets.add(new DataPacket("2 GB", "15 MIN", "20 SMS", "120 CUP", Uri.parse("tel:*133*1*4*2" + Uri.encode("#"))));
        dataPackets.add(new DataPacket("4 GB", "35 MIN", "40 SMS", "240 CUP", Uri.parse("tel:*133*1*4*3" + Uri.encode("#"))));
        dataPackets.add(new DataPacket("6 GB", "60 MIN", "70 SMS", "360 CUP", Uri.parse("tel:*133*1*4*4" + Uri.encode("#"))));
        carrierOffersGroups.add(new CarrierOffersGroup("Paquetes de Internet", dataPackets));

        ArrayList<BasicOffer> smsOffers = new ArrayList<>();
        smsOffers.add(new BasicOffer("20 SMS", "15 CUP", Uri.parse("tel:*133*2*1" + Uri.encode("#"))));
        smsOffers.add(new BasicOffer("50 SMS", "30 CUP", Uri.parse("tel:*133*2*2" + Uri.encode("#"))));
        smsOffers.add(new BasicOffer("90 SMS", "50 CUP", Uri.parse("tel:*133*2*3" + Uri.encode("#"))));
        smsOffers.add(new BasicOffer("120 SMS", "60 CUP", Uri.parse("tel:*133*2*4" + Uri.encode("#"))));
        carrierOffersGroups.add(new CarrierOffersGroup("Planes de SMS", smsOffers));

        ArrayList<BasicOffer> voiceOffers = new ArrayList<>();
        voiceOffers.add(new BasicOffer("5 MIN", "37.50 CUP", Uri.parse("tel:*133*3*1" + Uri.encode("#"))));
        voiceOffers.add(new BasicOffer("10 MIN", "72.50 CUP", Uri.parse("tel:*133*3*2" + Uri.encode("#"))));
        voiceOffers.add(new BasicOffer("15 MIN", "105 CUP", Uri.parse("tel:*133*3*3" + Uri.encode("#"))));
        voiceOffers.add(new BasicOffer("25 MIN", "162.50 CUP", Uri.parse("tel:*133*3*4" + Uri.encode("#"))));
        voiceOffers.add(new BasicOffer("40 MIN", "250 CUP", Uri.parse("tel:*133*3*5" + Uri.encode("#"))));
        carrierOffersGroups.add(new CarrierOffersGroup("Planes de Voz", voiceOffers));

        binding.shoppingFragmentRecycler.setAdapter(new ShoppingListAdapter(requireActivity(), carrierOffersGroups, obtainedUri -> Utils.performCall(this, obtainedUri)));
        binding.shoppingFragmentRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }
}