package cu.lt.joe.stk.objects;

import android.net.Uri;

public class DataPacket extends CarrierOffer
{
    private final String availableData, availableMinutes, availableMessages;

    public DataPacket(String availableData, String availableMinutes, String availableMessages, String price, Uri dialingUri)
    {
        super(price, dialingUri);
        this.availableData = availableData;
        this.availableMinutes = availableMinutes;
        this.availableMessages = availableMessages;
    }

    public String getAvailableData()
    {
        return availableData;
    }

    public String getAvailableMinutes()
    {
        return availableMinutes;
    }

    public String getAvailableMessages()
    {
        return availableMessages;
    }
}