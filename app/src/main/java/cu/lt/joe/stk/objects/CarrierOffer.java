package cu.lt.joe.stk.objects;

import android.net.Uri;

public class CarrierOffer
{
    private final String price;
    private final Uri dialingUri;

    public CarrierOffer(String price, Uri dialingUri)
    {
        this.price = price;
        this.dialingUri = dialingUri;
    }

    public String getPrice()
    {
        return price;
    }

    public Uri getDialingUri()
    {
        return dialingUri;
    }
}