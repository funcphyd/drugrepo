package com.example.myapp7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayList<String> itemList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the selected locale from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        String selectedLocale = sharedPreferences.getString("selected_locale", "en"); // Default to English

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.search_view);

        // Load data and set up the adapter
        itemList = loadJsonData(selectedLocale);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        // Set up the SearchView to filter the ListView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                if (itemList.contains(query)) {
                    showDrugDetails(query,selectedLocale);
                } else {
                    // Optionally handle the case where the query does not match any item
                    searchView.setQuery("", false); // Clear the query
                    searchView.clearFocus(); // Optionally clear focus
                }
                return true; // Return true to indicate the query has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list as the user types
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Set up item click listener for ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            if (selectedItem != null) {
                searchView.setQuery(selectedItem, false); // Populate SearchView with selected item
                searchView.clearFocus(); // Optionally clear focus to close the keyboard
            }
        });
    }

    private ArrayList<String> loadJsonData(String locale) {
        // Implement your JSON loading logic here
        // For simplicity, this method is not provided in this snippet
        ArrayList<String> data = new ArrayList<>();
        try {
            String filename = "";
            if("en".equalsIgnoreCase(locale)) {
                filename = "drugmodel.json";
            } else {
                filename = "drugmodel-"+locale+".json";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jarray = jsonObject.getAsJsonArray("drugs");
            for(int i=0;i<jarray.size();i++){
                JsonObject jsonObject1 = jarray.get(i).getAsJsonObject();
                String result = jsonObject1.get("drugname").getAsString();
                System.out.println("drugname "+result);

                data.add(result);
            }
            //Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            //data = new Gson().fromJson(reader, listType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return data;
        return data;
    }

    private void showDrugDetails(String drugName, String locale) {
        // For simplicity, use a static description or fetch from a database
        String drugDescription = "Description for " + drugName;

        // Create an Intent to start DrugDetailActivity
        Intent intent = new Intent(MainActivity.this, SideEffectsActivity.class);
        intent.putExtra("drug_name", drugName);
        intent.putExtra("drug_description", drugDescription);
        startActivity(intent);
    }
}
