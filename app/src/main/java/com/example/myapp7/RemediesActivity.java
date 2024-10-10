package com.example.myapp7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RemediesActivity extends BaseActivity {

    private TextView attributeTextView, valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);

        // Load the selected locale from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        String selectedLocale = sharedPreferences.getString("selected_locale", "en"); // Default to English


        // Find the TextViews in the layout
        attributeTextView = findViewById(R.id.attribute_text);
        valueTextView = findViewById(R.id.value_text);

        // Get the details passed from the previous activity
        String attribute = getIntent().getStringExtra("attribute");
        String value = getIntent().getStringExtra("value");

        // Set the text for the TextViews
        //attributeTextView.setText("Below of are the Remedies for the sideeffect: " + attribute);
        ArrayList<String> seList = loadJsonData(attribute, selectedLocale);
        seList.forEach(e-> valueTextView.setText("Remedy: " + e));
    }

    private ArrayList<String> loadJsonData(String symptomName, String locale) {
        System.out.print("The symtom received was: " + symptomName);
        // Implement your JSON loading logic here
        ArrayList<String> data = new ArrayList<>();
        try {

            String filename = "";
            if("en".equalsIgnoreCase(locale)) {
                filename = "drugmodel.json";
            } else {
                filename = "drugmodel-"+locale+".json";
            }
            System.out.println("Remedy filename loaded for the data is::" + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            JsonObject rootjsonObject = JsonParser.parseReader(reader)
                    .getAsJsonObject();
            JsonArray drugarray = rootjsonObject.getAsJsonArray("drugs");
            for(int i=0;i<drugarray.size();i++){
                JsonObject drugObject = drugarray.get(i).getAsJsonObject();
                JsonArray searray = drugObject.getAsJsonArray("sideeffects");
                for(int j=0;j<searray.size();j++) {
                    JsonObject  seObject = searray.get(j).getAsJsonObject();
                    String seResult = seObject.get("sname").getAsString();
                    System.out.println("Remedies --->>  Side Effect Name --" + seResult);
                    if (seResult.equals(symptomName)) {
                        JsonArray remarray = seObject.getAsJsonArray("Remedies");
                        for(int k=0;k<remarray.size();k++) {
                            JsonObject remObject = remarray.get(k).getAsJsonObject();
                            String remResult = remObject.get("rdescription").getAsString();
                            System.out.println("Remedy  --" + remResult);
                            data.add(remResult);
                        }
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
