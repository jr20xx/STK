package cu.lt.joe.stk.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;
import java.util.Locale;
import cu.lt.joe.stk.Constants;

public class LanguageUtils
{
    public static String getCurrentAppLocaleTag(Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SAVED_LOCALE_TAG, null);
        else
        {
            String currentLocaleTag = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            if (!currentLocaleTag.isBlank())
                return currentLocaleTag.toLowerCase().substring(0, 2);
            return currentLocaleTag;
        }
    }

    public static void updateAppLocale(String languageTag, Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.SAVED_LOCALE_TAG, languageTag).apply();
        if (languageTag == null)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        else
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag));
    }

    public static Context getLocalizedContext(Context baseContext)
    {
        String savedLanguageTag = PreferenceManager.getDefaultSharedPreferences(baseContext).getString(Constants.SAVED_LOCALE_TAG, null);
        if (savedLanguageTag != null)
        {
            Locale customLocale = Locale.forLanguageTag(savedLanguageTag);
            Locale.setDefault(customLocale);
            Configuration customConfiguration = new Configuration(baseContext.getResources().getConfiguration());
            customConfiguration.setLocale(customLocale);
            return baseContext.createConfigurationContext(customConfiguration);
        }
        return baseContext;
    }
}