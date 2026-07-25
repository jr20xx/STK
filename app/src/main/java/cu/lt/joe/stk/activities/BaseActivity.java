package cu.lt.joe.stk.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;

public class BaseActivity extends AppCompatActivity
{
    protected final String IS_DARK_MODE_ENABLED = "IS_DARK_MODE_ENABLED";
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getBoolean(IS_DARK_MODE_ENABLED, false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        applyDynamicColors();
    }

    protected void applyDynamicColors()
    {
        DynamicColors.applyToActivityIfAvailable(this);
    }
}