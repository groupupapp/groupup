package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
// made by Umit Kadiroglu
public class ForgotActivity extends AppCompatActivity {
    //design
    EditText edittext_email_forgot;
    MaterialButton button_reset;
    ProgressBar progress_reset;
    TextInputLayout til_email_forgot;

    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        //design
        edittext_email_forgot = findViewById(R.id.edittext_email_forgot);
        button_reset = findViewById(R.id.button_reset);
        progress_reset = findViewById(R.id.progress_reset);
        til_email_forgot = findViewById(R.id.til_email_forgot);
        //firebase
        mAuth = FirebaseAuth.getInstance();

        //reset button
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edittext_email_forgot.getText().toString().trim();
                //email validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email_forgot.setError(getResources().getString(R.string.email_error));
                    edittext_email_forgot.setFocusable(true);
                } else {
                    beginRecovery(email);
                }
            }
        });
    }

    //password recovery
    private void beginRecovery(String email) {
        progress_reset.setVisibility(View.VISIBLE);
        button_reset.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progress_reset.setVisibility(View.INVISIBLE);
                    button_reset.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgotActivity.this, getResources().getString(R.string.email_sent), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_reset.setVisibility(View.INVISIBLE);
                button_reset.setVisibility(View.VISIBLE);
                Toast.makeText(ForgotActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}