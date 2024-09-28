package com.example.myapp7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SideEffectsActivity extends BaseActivity {

    private TableLayout tableLayout;
    private TextView tableHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_effects);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);

        // Load the selected locale from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        String selectedLocale = sharedPreferences.getString("selected_locale", "en"); // Default to English

        // Find the heading TextView
        tableHeading = findViewById(R.id.table_heading);
        tableLayout = findViewById(R.id.table_layout);

        // Get the drug details passed from the MainActivity
        String drugName = getIntent().getStringExtra("drug_name");
        String drugDescription = getIntent().getStringExtra("drug_description");

        ArrayList<String> seList = loadJsonData(drugName, selectedLocale);
        seList.forEach(e-> addRow(e,"lnk1"));
    }

    // Method to add a row to the TableLayout
    private void addRow(String attribute, String value) {
        // Create a new TableRow
        TableRow tableRow = new TableRow(this);

        // Create the attribute TextView (first column)
        TextView attributeTextView = new TextView(this);
        attributeTextView.setText(attribute);
        attributeTextView.setPadding(8, 8, 8, 8);
        attributeTextView.setGravity(android.view.Gravity.START);
        attributeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        // Apply the border to the attribute TextView
        attributeTextView.setBackgroundResource(R.drawable.border);

        // Create the value TextView (second column)
        TextView valueTextView = new TextView(this);
        valueTextView.setText(value);
        valueTextView.setPadding(8, 8, 8, 8);
        valueTextView.setGravity(android.view.Gravity.START);
        valueTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        // Apply the border to the value TextView
        valueTextView.setBackgroundResource(R.drawable.border);

        // Add the TextViews to the TableRow
        tableRow.addView(attributeTextView);

        // Add SeekBar for rows that require a rating scale for the side effect
        if ("lnk1".equals(value)) {
            // Create a SeekBar for the row (scale of 1-5)
            SeekBar seekBar = new SeekBar(this);
            seekBar.setMax(4); // Max is 4 because the range is 0-4, but we display it as 1-5
            seekBar.setProgress(2); // Default to middle value (3)

            // Set layout params to ensure it fits in the row
            seekBar.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            // Add listener to the SeekBar
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Show the rating as 1-5 (progress + 1)
                    Toast.makeText(SideEffectsActivity.this, "Scale: " + (progress + 1), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Optional: You can handle events when the user starts interacting with the SeekBar
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Optional: Handle event when the user stops moving the SeekBar
                }
            });

            // Add the SeekBar to the TableRow
            tableRow.addView(seekBar);
        } else {
            // For rows without SeekBar, just add the value TextView
            tableRow.addView(valueTextView);
        }

        // Add click listener to the TableRow
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the MoreFruitDetailsActivity
                Intent intent = new Intent(SideEffectsActivity.this, RemediesActivity.class);

                // Pass the attribute and value to the next activity
                intent.putExtra("attribute", attribute);
                intent.putExtra("value", value);

                // Start the next activity
                startActivity(intent);
            }
        });

        // Add the TableRow to the TableLayout
        tableLayout.addView(tableRow);
    }

    private ArrayList<String> loadJsonData(String drugName, String locale) {
        // Implement your JSON loading logic here
        ArrayList<String> data = new ArrayList<>();
        try {

            String filename = "";
            if("en".equalsIgnoreCase(locale)) {
                filename = "drugmodel.json";
            } else {
                filename = "drugmodel-"+locale+".json";
            }
            System.out.println("filename loaded for the data is::" + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            JsonObject rootjsonObject = JsonParser.parseReader(reader)
                                                    .getAsJsonObject();
            JsonArray drugarray = rootjsonObject.getAsJsonArray("drugs");
            for(int i=0;i<drugarray.size();i++){
                JsonObject drugObject = drugarray.get(i).getAsJsonObject();
                String result = drugObject.get("drugname").getAsString();;
                if (result.equals(drugName)) {
                    System.out.println("Drug name match found");
                    JsonArray searray = drugObject.getAsJsonArray("sideeffects");
                    for(int j=0;j<searray.size();j++) {
                        JsonObject  seObject = searray.get(j).getAsJsonObject();
                        String seResult = seObject.get("sname").getAsString();
                        System.out.println("Side Effect Name --" + seResult);
                        data.add(seResult);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return data;
        return data;
    }
}

