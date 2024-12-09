package com.example.fpwmp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btnusr;
    private EditText mail, name, id;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnusr = findViewById(R.id.NxtbtnUsr);
        name = findViewById(R.id.Name);
        id = findViewById(R.id.ID);
        mail = findViewById(R.id.Mail);
        db = FirebaseFirestore.getInstance();

        btnusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = id.getText().toString();
                String usertName = name.getText().toString();
                String userMail = mail.getText().toString();

                // check validity of data
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(usertName) || TextUtils.isEmpty(userMail)) {
                    Toast.makeText(MainActivity.this, "ID, Mail and Name are required", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidEmail(userMail)) {
                        Toast.makeText(MainActivity.this, "Invalid email format. It must contain '@'", Toast.LENGTH_SHORT).show();
                    } else {
                    // add to firebase via a function
                    addUser(userId, usertName, userMail);

                    Intent intent = new Intent(MainActivity.this, Admin_Login.class);
                    startActivity(intent);

                    // empty the fields again
                    id.setText("");
                    name.setText("");
                    mail.setText("");
                }

            }
        }
    });
    }

    private boolean isValidEmail(String email) {
        // Simple regex to check if the email contains '@' and a domain
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String loggedInAdmin = sharedPreferences.getString("loggedInAdmin", null);

        if (loggedInAdmin != null) {
            // Redirect to MainActivity if the admin is already logged in
            Intent intent = new Intent(MainActivity.this, Admin_Login.class);
            startActivity(intent);
            finish();
        }
    }

    private void addUser(String Id, String Name, String Mail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a Map for user data
        Map<String, Object> user = new HashMap<>();
        user.put("userId", Id);
        user.put("userName", Name);
        user.put("userMail", Mail);

        // Get the User collection and document reference
        CollectionReference collections = db.collection("User");
        DocumentReference doc = collections.document(Id);

        // Save the user data to Firestore
        doc.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}