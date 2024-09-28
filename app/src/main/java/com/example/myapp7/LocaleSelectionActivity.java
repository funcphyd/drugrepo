package com.example.myapp7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LocaleSelectionActivity extends AppCompatActivity {

    private Button englishButton, spanishButton, frenchButton, teluguButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale_selection);

        sharedPreferences = getSharedPreferences("LocalePrefs", MODE_PRIVATE);

        englishButton = findViewById(R.id.english_button);
        spanishButton = findViewById(R.id.spanish_button);
        frenchButton = findViewById(R.id.french_button);
        teluguButton = findViewById(R.id.telugu_button);

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });

        spanishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("es");
            }
        });

        frenchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
            }
        });

        teluguButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("te");
            }
        });
    }

    private void setLocale(String localeCode) {
        // Store the selected locale in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_locale", localeCode);
        editor.apply();

        // Set the locale
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);

        // Update the configuration with the new locale
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restart the app or navigate to the main screen
        Intent intent = new Intent(LocaleSelectionActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
