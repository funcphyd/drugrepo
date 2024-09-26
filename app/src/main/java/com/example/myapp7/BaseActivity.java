package com.example.myapp7;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply the locale before setting the content view
        applyLocale();
    }

    private void applyLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        String selectedLocale = sharedPreferences.getString("selected_locale", "en"); // Default to English

        Locale locale = new Locale(selectedLocale);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}