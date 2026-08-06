package cu.lt.joe.stk.objects;

public class ShortcutItem extends BaseItem
{
    private final int iconResId;
    private final String shortTitle, longTitle, dialingNumber;

    public ShortcutItem(int iconResId, String shortTitle, String longTitle, String dialingNumber)
    {
        this.iconResId = iconResId;
        this.shortTitle = shortTitle;
        this.dialingNumber = dialingNumber;
        this.longTitle = longTitle;
    }

    public int getIconResId()
    {
        return iconResId;
    }

    public String getShortTitle()
    {
        return shortTitle;
    }

    public String getLongTitle()
    {
        return longTitle;
    }

    public String getDialingNumber()
    {
        return dialingNumber;
    }
}