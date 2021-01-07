package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText edittext_email_login, edittext_password_login;
    MaterialButton button_login;
    TextView text_not_have_account, text_forgot_password;
    TextInputLayout til_email_login, til_password_login;
    private FirebaseAuth mAuth;
    ProgressBar progress_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edittext_email_login = findViewById(R.id.edittext_email_login);
        edittext_password_login = findViewById(R.id.edittext_password_login);
        button_login = findViewById(R.id.button_login);
        text_not_have_account = findViewById(R.id.text_not_have_account);
        text_forgot_password = findViewById(R.id.text_forgot_password);
        til_email_login = findViewById(R.id.til_email_login);
        til_password_login = findViewById(R.id.til_password_login);
        progress_login = findViewById(R.id.progress_login);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //login button
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edittext_email_login.getText().toString().trim();
                String password = edittext_password_login.getText().toString().trim();
                til_email_login.setError(null);
                til_password_login.setError(null);
                //email and password kontrolü
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email_login.setError(getResources().getString(R.string.email_error));
                    edittext_email_login.setFocusable(true);
                } else if (password.length() < 6) {
                    til_password_login.setError(getResources().getString(R.string.password_error));
                    edittext_password_login.setFocusable(true);
                } else {
                    loginUser(email, password);
                }
            }
        });
        //not have account?
        text_not_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        //forgot password?
        text_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                finish();
            }
        });
    }
    //giriş yap
    private void loginUser(String email, String password) {
        progress_login.setVisibility(View.VISIBLE);
        button_login.setVisibility(View.INVISIBLE);
        //firebase loginuser
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finishAffinity();
                        } else {
                            progress_login.setVisibility(View.INVISIBLE);
                            button_login.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_login.setVisibility(View.INVISIBLE);
                button_login.setVisibility(View.VISIBLE);
                til_password_login.setError(getResources().getString(R.string.invalid_password));
            }
        });
    }
}