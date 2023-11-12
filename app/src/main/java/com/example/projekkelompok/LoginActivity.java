package com.example.projekkelompok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fireBaseAuth;
    View view;
    Button btnLogin;
    TextView tvEmail, tvPass, tvLogin, tvSignUp1, tvSignUp2, tvForgetPass;
    EditText etEmail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        view = findViewById(R.id.view);
        btnLogin = findViewById(R.id.btnLogin);
        tvEmail = findViewById(R.id.tvEmail);
        tvPass = findViewById(R.id.tvPass);
        tvLogin = findViewById(R.id.tvLogin);
        tvForgetPass = findViewById(R.id.forgetPass);
        tvSignUp1 = findViewById(R.id.tvSignUp1);
        tvSignUp2 = findViewById(R.id.tvSignUp2);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        fireBaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                login(email, pass);
            }
        });

        tvSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login(String email, String password) {
        fireBaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}