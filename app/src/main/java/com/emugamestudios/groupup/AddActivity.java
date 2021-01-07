package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AddActivity extends AppCompatActivity {
    ImageView image_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //sağ üstteki create tuşu
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.create_group);
        // grup fotosu ekleme
        image_group = findViewById(R.id.image_group);
        Glide.with(AddActivity.this).load(R.drawable.ic_add_image_grey).circleCrop().into(image_group);
        image_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetContainer));

                bottomSheetView.findViewById(R.id.button_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //galeri açılacak
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //camera açılacak
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //create tuşuna basıldığındaki işlem
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            //eklenecek
        }
        return super.onOptionsItemSelected(item);
    }
}