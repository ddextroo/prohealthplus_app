package com.ddev.prohealth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DebugActivity extends Activity {

    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        // Initialize your UI components
        errorTextView = findViewById(R.id.errorTextView);

        // Retrieve error message from intent
        String errorMessage = getIntent().getStringExtra("errorMessage");
        if (errorMessage != null) {
            displayError(errorMessage);
        }
    }

    private void displayError(String errorMessage) {
        // Display the error message in your UI components
        errorTextView.setText(errorMessage);
        Log.e("DebugActivity", errorMessage); // Log the error for further analysis
    }
}

