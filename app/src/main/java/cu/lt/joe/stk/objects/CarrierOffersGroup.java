package cu.lt.joe.stk.objects;

import java.util.ArrayList;

public class CarrierOffersGroup
{
    private final String header;
    private final ArrayList carrierOffers;

    public CarrierOffersGroup(String header, ArrayList carrierOffers)
    {
        this.header = header;
        this.carrierOffers = carrierOffers;
    }

    public String getHeader()
    {
        return header;
    }

    public ArrayList getCarrierOffers()
    {
        return carrierOffers;
    }
}