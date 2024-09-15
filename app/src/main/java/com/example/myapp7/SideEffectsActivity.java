package com.example.myapp7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SideEffectsActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TextView tableHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_effects);

        // Find the heading TextView
        tableHeading = findViewById(R.id.table_heading);
        tableLayout = findViewById(R.id.table_layout);

        // Get the drug details passed from the MainActivity
        String drugName = getIntent().getStringExtra("drug_name");
        String drugDescription = getIntent().getStringExtra("drug_description");

        // Set the heading to include the fruit name
        tableHeading.setText("Side Effects for the Drug: "+drugName);

        ArrayList<String> seList = loadJsonData(drugName);

        // Example additional drug details (you can replace or expand with real data)
        String[] drugAttributes = {"SideEffect Name", "SideEffect Description", "link"};
        String[] drugValues = {drugName, drugDescription, "Link"};

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
        tableRow.addView(valueTextView);

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

    private ArrayList<String> loadJsonData(String drugName) {
        // Implement your JSON loading logic here
        ArrayList<String> data = new ArrayList<>();
        try {
            System.out.println("In the load method");
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("drugmodel.json")));
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

