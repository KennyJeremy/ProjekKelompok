package com.example.projekkelompok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekkelompok.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth fireBaseAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String onlineUserId;

    View view;
    TextView tvSignUp, tvName, tvEmail, tvPhoneNumber, tvPass, tvRePass;
    EditText etName, etEmail, etPhoneNumber, etPass, etRePass;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        view = findViewById(R.id.view);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhone);
        tvPass = findViewById(R.id.tvPass);
        tvRePass = findViewById(R.id.tvRePass);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);

        btSignUp = findViewById(R.id.btnSignUp);

        fireBaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String password = etPass.getText().toString();
                String repeatedPassword = etRePass.getText().toString();

                if (repeatedPassword.trim().equalsIgnoreCase(password.trim())){
                    register(name, email, phoneNumber, password);

                    reference = FirebaseDatabase.getInstance().getReference("users");

                    UserModel user = new UserModel(name, email, phoneNumber, password);

                    reference.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "You have signup successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else Toast.makeText(SignupActivity.this, "Password tidak cocok. Silahkan ulangi!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(String name, String email, String phone, String pass) {
        fireBaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
