package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {
    EditText edittext_email, edittext_password, edittext_name;
    TextView text_have_account;
    TextInputLayout til_email, til_password, til_name, til_school, til_department;
    MaterialButton button_register;
    ProgressBar progress_register;
    private FirebaseAuth mAuth;
    AutoCompleteTextView school_spinner, department_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        til_name = findViewById(R.id.til_name);
        til_school = findViewById(R.id.til_school);
        til_department = findViewById(R.id.til_department);
        edittext_name = findViewById(R.id.edittext_name);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        text_have_account = findViewById(R.id.text_have_account);
        edittext_email = findViewById(R.id.edittext_email);
        edittext_password = findViewById(R.id.edittext_password);
        button_register = findViewById(R.id.button_register);
        progress_register = findViewById(R.id.progress_register);
        department_spinner = findViewById(R.id.department_spinner);
        school_spinner = findViewById(R.id.school_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.universityList, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.department, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school_spinner.setAdapter(adapter);
        department_spinner.setAdapter(adapter2);

        school_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //close keyboard after selection
                hideSoftKeyboard(RegisterActivity.this);
            }
        });

        department_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //close keyboard after selection
                hideSoftKeyboard(RegisterActivity.this);
            }
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //register button
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edittext_email.getText().toString().trim();
                String password = edittext_password.getText().toString().trim();
                String name = edittext_name.getText().toString().trim();
                String school = school_spinner.getText().toString().trim();
                String department = department_spinner.getText().toString().trim();

                til_email.setError(null);
                til_password.setError(null);
                til_name.setError(null);
                til_school.setError(null);
                til_school.setError(null);

                //email and password validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    til_email.setError(getResources().getString(R.string.email_error));
                    edittext_email.setFocusable(true);
                }else if (password.length()<6){
                    til_password.setError(getResources().getString(R.string.password_error));
                    edittext_password.setFocusable(true);
                }else if(name.length()<1){
                    til_name.setError(getResources().getString(R.string.name_error));
                    edittext_name.setFocusable(true);
                }else if(school.length()<1){
                    til_school.setError(getResources().getString(R.string.school_error));
                    school_spinner.setFocusable(true);
                }else if(department.length()<1){
                    til_department.setError(getResources().getString(R.string.department_error));
                    department_spinner.setFocusable(true);
                } else{
                    registerUser(email, password, name, school, department);
                }
            }
        });

        //have an account?
        text_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password, final String name, final String school, final String department) {
        button_register.setVisibility(View.INVISIBLE);
        progress_register.setVisibility(View.VISIBLE);
        //firebase sign up
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progress_register.setVisibility(View.INVISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            //get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("bio", "");
                            hashMap.put("name", name);
                            hashMap.put("uni", school);
                            hashMap.put("department", department);
                            hashMap.put("photo", "https://cdn.discordapp.com/attachments/784152625662132235/793974769598201876/44884218_345707102882519_2446069589734326272_n.jpg");
                            //firebase database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);
                            //change activity to main page
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));

                            //close all activities
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            progress_register.setVisibility(View.INVISIBLE);
                            button_register.setVisibility(View.VISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_register.setVisibility(View.INVISIBLE);
                button_register.setVisibility(View.VISIBLE);
                til_email.setError(getResources().getString(R.string.email_in_use));
                edittext_email.setFocusable(true);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}