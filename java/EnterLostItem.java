package com.example.wmpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wmpfinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = itemName.getText().toString().trim();
                String description = itemDescription.getText().toString().trim();
                String location = locationLastSeen.getText().toString().trim();
                String date = dateLost.getText().toString().trim();
                String email = contactDetails.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location) || TextUtils.isEmpty(date) || TextUtils.isEmpty(email)) {
                    Toast.makeText(EnterLostItem.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidEmail(email)) {
                        Toast.makeText(EnterLostItem.this, "Invalid email format. It must contain '@'", Toast.LENGTH_SHORT).show();
                    } else {
                        SubmitData(name, description, location, date, email);
                        Toast.makeText(EnterLostItem.this, "Lost item submitted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EnterLostItem.this, UserChoose.class);
                        startActivity(intent);

                        itemName.setText("");
                        itemDescription.setText("");
                        locationLastSeen.setText("");
                        dateLost.setText("");
                        contactDetails.setText("");
                    }
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void SubmitData(String Name, String Description, String Location, String Date, String Email) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("ItemName", Name);
        user.put("ItemDescription", Description);
        user.put("LastLocation", Location);
        user.put("Date", Date);
        user.put("userMail", Email);

        CollectionReference collections = db.collection("LostItem");
        DocumentReference doc = collections.document(Email);

        doc.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EnterLostItem.this, "Lost item added successfully", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EnterLostItem.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
