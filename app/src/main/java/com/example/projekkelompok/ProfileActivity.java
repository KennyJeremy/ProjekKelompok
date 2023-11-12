package com.example.projekkelompok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String currentUserEmail;
    View view;
    TextView tvUsername, tvEmail, tvPhone, tvPass;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        view = findViewById(R.id.view);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvPass = findViewById(R.id.tvPass);
        btnHome = findViewById(R.id.btnHome);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        currentUserEmail = mAuth.getCurrentUser().getEmail();

        showUserData();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showUserData(){
        reference.child(currentUserEmail.substring(0, currentUserEmail.indexOf("@"))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String pass = snapshot.child("password").getValue(String.class);

                tvUsername.setText("Your Name = " + name);
                tvEmail.setText("Your Email = " + email);
                tvPhone.setText("Your Phone Number = " + phone);
                tvPass.setText("Your Password = " + pass);

                Log.d("ProfileActivity", "User profile data retrieved successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileActivity", "Error retrieving user profile data: " + error.getMessage());
            }
        });


    }

}
