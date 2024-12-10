package com.example.wmpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserChoose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose); // Ensure the layout file is named correctly

        // Get references to the CardViews
        CardView cardLostItem = findViewById(R.id.lostitem);
        CardView cardUploadItem = findViewById(R.id.uploaditem);

        // Set onClickListener for "Enter Lost Item"
        cardLostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EnterLostItemActivity
                Intent intent = new Intent(MainActivity.this, EnterLostItem.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for "Upload Lost Item"
        cardUploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadLostItemActivity
                Intent intent = new Intent(MainActivity.this, UploadImageDB.class);
                startActivity(intent);
            }
        });
    }
}
