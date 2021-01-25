package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

// made by Umit Kadiroglu

public class AddActivity extends AppCompatActivity {
    //design
    ImageView image_group;
    TextInputLayout til_groupname, til_description;
    EditText edittext_groupname, edittext_groupdescription;
    ProgressBar progress_add_group;

    //firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String name, department, uid, uni, email, photo;
    Uri image_uri = null;

    //permissions
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    String[] cameraPermissions;
    String[] storagePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //init
        til_groupname = findViewById(R.id.til_groupname);
        til_description = findViewById(R.id.til_description);
        edittext_groupname = findViewById(R.id.edittext_groupname);
        edittext_groupdescription = findViewById(R.id.edittext_groupdescription);
        progress_add_group = findViewById(R.id.progress_add_group);

        //permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        //user info
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    department = "" + ds.child("department").getValue();
                    uni = "" + ds.child("uni").getValue();
                    photo = "" + ds.child("photo").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //create button on action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.create_group);

        //add group photo with bottomsheetdialog
        image_group = findViewById(R.id.image_group);
        Picasso.get().load(R.drawable.ic_add_image_grey).into(image_group);
        image_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetContainer));
                bottomSheetView.findViewById(R.id.button_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkStoragePermission()) {
                            requestStoragePermission();
                        } else {
                            pickFromGallery();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetView.findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkCameraPermission()) {
                            requestCameraPermission();
                        } else {
                            pickFromCamera();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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

    //on click create
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            String group_name = edittext_groupname.getText().toString();
            String group_description = edittext_groupdescription.getText().toString();
            til_groupname.setError(null);
            til_description.setError(null);

            if (group_name.length() < 1) {
                til_groupname.setError(getResources().getString(R.string.set_group_name_error));
                edittext_groupname.setFocusable(true);
            } else if (group_description.length() < 1) {
                til_description.setError(getResources().getString(R.string.set_group_desc_error));
                edittext_groupdescription.setFocusable(true);
            } else {
                if (image_uri == null) {
                    //post without image
                    uploadData(group_name, group_description, "noImage");
                } else {
                    uploadData(group_name, group_description, String.valueOf(image_uri));
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //create group
    private void uploadData(final String title, final String description, String uri) {
        progress_add_group.setVisibility(View.VISIBLE);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Groups/" + "group_" + timestamp;
        if (!uri.equals("noImage")) {
            //group with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("OnSuccess");
                            final Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (uriTask.isSuccessful()) {
                                        String downloadUri = uriTask.getResult().toString();
                                        System.out.println("isSuccessful");
                                        HashMap<Object, String> hashMapMembers = new HashMap<>();
                                        hashMapMembers.put(uid, "Founder");
                                        DatabaseReference refMembers = FirebaseDatabase.getInstance().getReference("Members");
                                        refMembers.child(timestamp).setValue(hashMapMembers);

                                        HashMap<Object, String> hashMap = new HashMap<>();
                                        hashMap.put("uid", uid);
                                        hashMap.put("name", name);
                                        hashMap.put("email", email);
                                        hashMap.put("groupdId", timestamp);
                                        hashMap.put("groupTitle", title);
                                        hashMap.put("groupDescription", description);
                                        hashMap.put("groupPhoto", downloadUri);
                                        hashMap.put("department", department);
                                        hashMap.put("uni", uni);
                                        hashMap.put("photo", photo);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                                        ref.child(timestamp).setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progress_add_group.setVisibility(View.INVISIBLE);
                                                        edittext_groupname.setText("");
                                                        edittext_groupdescription.setText("");
                                                        image_group.setImageURI(null);
                                                        image_uri = null;
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progress_add_group.setVisibility(View.INVISIBLE);
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress_add_group.setVisibility(View.INVISIBLE);
                    finish();

                }
            });
        } else {
            //group with no image
            HashMap<Object, String> hashMapMembers = new HashMap<>();
            hashMapMembers.put(uid, "Founder");
            DatabaseReference refMembers = FirebaseDatabase.getInstance().getReference("Members");
            refMembers.child(timestamp).setValue(hashMapMembers);

            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid", uid);
            hashMap.put("name", name);
            hashMap.put("email", email);
            hashMap.put("groupdId", timestamp);
            hashMap.put("groupTitle", title);
            hashMap.put("groupDescription", description);
            hashMap.put("groupPhoto", "https://cdn.discordapp.com/attachments/784152625662132235/798317217926217728/sonlogo.png");
            hashMap.put("department", department);
            hashMap.put("uni", uni);
            hashMap.put("photo", photo);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
            ref.child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progress_add_group.setVisibility(View.INVISIBLE);
                            edittext_groupname.setText("");
                            edittext_groupdescription.setText("");
                            image_group.setImageURI(null);
                            image_uri = null;
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress_add_group.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    });
        }
    }

    //gallery
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    //camera
    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    //check permission
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    //request permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    //check permission
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    //request permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //grant permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    }
                }
            }
            break;
        }
    }

    //set image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                image_group.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                image_group.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}