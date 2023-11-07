package com.example.connectwave.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectwave.R;
import com.example.connectwave.Model.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    CircleImageView profileview;
    Uri image;
    String ImageURI ;
    EditText reg_name, reg_email, reg_pass, reg_conform_pass;
    Button regiserbtn;
//    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    String status = "Hey! i am using this application";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);

        TextView loginhere = findViewById(R.id.loginhere);
        profileview = findViewById(R.id.profile_image);
        reg_pass = findViewById(R.id.register_pass);
        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.register_email);
        regiserbtn = findViewById(R.id.register_btn);
        reg_conform_pass = findViewById(R.id.conforom_pass);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        regiserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name, email, pass, conform_pass;
                name = reg_name.getText().toString();
                email = reg_email.getText().toString();
                pass = reg_pass.getText().toString();
                conform_pass = reg_conform_pass.getText().toString();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name) || TextUtils.isEmpty(conform_pass)) {
                    Toast.makeText(Register.this, "please enter valid data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
//                else if (email.matches(emailPattern)) {
//                    reg_email.setError("invalid email");
//                    Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_SHORT).show();
//                }
                else if (!pass.equals(conform_pass)) {
                    Toast.makeText(getApplicationContext(), "password not match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (pass.length() < 8) {
                    reg_email.setError("invalid password");
                    Toast.makeText(getApplicationContext(), "8 characters should required", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Register.this, "redi", Toast.LENGTH_SHORT).show();
                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
                                if(image!=null)
                                {
                                    storageReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        ImageURI=uri.toString();
                                                        users users = new users(auth.getUid(),name,email,ImageURI,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    startActivity(new Intent(getApplicationContext(),Home.class));
                                                                }
                                                                else {
                                                                    Toast.makeText(Register.this, "Error in creating a new user!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {

                                    progressDialog.dismiss();
                                    ImageURI= "https://firebasestorage.googleapis.com/v0/b/connectwave-19684.appspot.com/o/User-Profile-PNG-Image.png?alt=media&token=2a89950d-799f-4dc9-8813-494d50242fe2";
                                    users users = new users(auth.getUid(),name,email,ImageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                startActivity(new Intent(getApplicationContext(),Home.class));
                                            }
                                            else {
                                                Toast.makeText(Register.this, "Error in creating a new user!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                }
                            else {
                                Toast.makeText(Register.this, "something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        profileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
        loginhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                image = data.getData();
                profileview.setImageURI(image);
            }
        }
    }
}
