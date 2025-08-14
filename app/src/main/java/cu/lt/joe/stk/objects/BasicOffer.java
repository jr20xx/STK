package cu.lt.joe.stk.objects;

import android.net.Uri;

public class BasicOffer extends CarrierOffer
{
    private final String availableService;

    public BasicOffer(String availableService, String price, Uri dialingUri)
    {
        super(price, dialingUri);
        this.availableService = availableService;
    }

    public String getAvailableService()
    {
        return availableService;
    }
}