package com.example.projectwmp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwmp.R;

public class EnterLostItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_lost_item);

        // Link UI elements
        EditText itemName = findViewById(R.id.item_name);
        EditText itemDescription = findViewById(R.id.item_description);
        EditText locationLastSeen = findViewById(R.id.location_last_seen);
        EditText dateLost = findViewById(R.id.date_lost);
        EditText contactDetails = findViewById(R.id.contact_details);
        Button submitButton = findViewById(R.id.submit_button);

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> {
            // Get input from fields
            String name = itemName.getText().toString().trim();
            String description = itemDescription.getText().toString().trim();
            String location = locationLastSeen.getText().toString().trim();
            String date = dateLost.getText().toString().trim();
            String email = contactDetails.getText().toString().trim();

            // Validate the inputs
            if (name.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || email.isEmpty()) {
                Toast.makeText(EnterLostItem.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(EnterLostItem.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                return;
            }

            // All inputs are valid
            Toast.makeText(EnterLostItem.this, "Lost item submitted successfully!", Toast.LENGTH_SHORT).show();
        });
    }
}
