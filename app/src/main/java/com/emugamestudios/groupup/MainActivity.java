package com.emugamestudios.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
// made by Umit Kadiroglu
public class MainActivity extends AppCompatActivity {
    //design
    MaterialButton button_register_main, button_login_main;
    TextView text_pp_and_tos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //design
        button_register_main = findViewById(R.id.button_register_main);
        button_login_main = findViewById(R.id.button_login_main);
        text_pp_and_tos = findViewById(R.id.text_pp_and_tos);

        //register button
        button_register_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //login button
        button_login_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //privacy policy and terms of service
        text_pp_and_tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pp ve tos eklenecek
            }
        });
    }
}