package com.example.wmpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Admin_Login extends AppCompatActivity {
    private Button lgn;
    private EditText maill, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        maill = findViewById(R.id.maill);
        pass = findViewById(R.id.pass);
        lgn = findViewById(R.id.nextlgn);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminMail = maill.getText().toString();
                String adminPass = pass.getText().toString();
                if (TextUtils.isEmpty(adminMail) || TextUtils.isEmpty(adminPass)) {
                    Toast.makeText(Admin_Login.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                } else {
                    readAdmin(adminMail, adminPass);
                    Intent intent = new Intent(Admin_Login.this, UploadImageDB.class);
                    startActivity(intent);
                    maill.setText("");
                    pass.setText("");
                }
            }
        });
    }

    private void readAdmin(String maill, String pass) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference adminCollection = db.collection("Admin");

        Log.d("Admin_Login", "Querying for admin with Mail: " + maill);
        Log.d("Admin_Login", "Entered Password: " + pass);

        adminCollection.whereEqualTo("Mail", maill).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        Log.d("Admin_Login", "Documents found: " + querySnapshot.size());
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String storedPass = documentSnapshot.getString("Password");
                            Log.d("Admin_Login", "Stored Password: " + storedPass);
                            if (storedPass != null && storedPass.equals(pass)) {
                                Toast.makeText(Admin_Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Admin_Login.this, UploadImageDB.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Admin_Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("Admin_Login", "Admin not found with email: " + maill);
                            Toast.makeText(Admin_Login.this, "Admin not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Admin_Login", "Error: " + e.getMessage());
                        Toast.makeText(Admin_Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
