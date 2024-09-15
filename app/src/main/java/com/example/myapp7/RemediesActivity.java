package com.example.myapp7;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RemediesActivity extends AppCompatActivity {

    private TextView attributeTextView, valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);

        // Find the TextViews in the layout
        attributeTextView = findViewById(R.id.attribute_text);
        valueTextView = findViewById(R.id.value_text);

        // Get the details passed from the previous activity
        String attribute = getIntent().getStringExtra("attribute");
        String value = getIntent().getStringExtra("value");

        // Set the text for the TextViews
        attributeTextView.setText("Below of are the Remedies for the sideeffect: " + attribute);
        valueTextView.setText("Rememdy: " + value);
    }
}
