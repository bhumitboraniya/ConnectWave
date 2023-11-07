package com.example.connectwave.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectwave.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth auth ;
    TextView signuphere;
    EditText login_email, login_password;
//    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signuphere =  findViewById(R.id.signuphere);
        signuphere.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent  i = new Intent(getApplicationContext(),Register.class);
                        startActivity(i);
                    }
                }
        );
        Button loginbtn = findViewById(R.id.loginbtn);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_pass);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();
                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login.this, "enter valid data", Toast.LENGTH_SHORT).show();
                }
//                else if(email.matches(emailPattern)){
//                    login_email.setError("invalid email");
//                    Toast.makeText(Login.this, "envalid email", Toast.LENGTH_SHORT).show();
//                }
                else if (password.length()<8) {
                    login_email.setError("invalid password");
                    Toast.makeText(Login.this, "8 characters should required", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(getApplicationContext(),Home.class));
                            }
                            else {
                                Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }
}