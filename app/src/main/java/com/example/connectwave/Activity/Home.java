package com.example.connectwave.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connectwave.R;
import com.example.connectwave.Adapter.UserAdapter;
import com.example.connectwave.Model.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecycleView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<users> usersArrayList;
    ImageView logout;
    ImageView settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        settings = findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });
        logout = findViewById(R.id.image_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Home.this,R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesbtn,nobtn;
                yesbtn = dialog.findViewById(R.id.yesbtn);
                nobtn= dialog.findViewById(R.id.nobtn);
                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Register.class));
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();

        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    users user = dataSnapshot.getValue(users.class);
                    usersArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mainUserRecycleView = findViewById(R.id.mainuserrecycleview);
        mainUserRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(Home.this,usersArrayList);
        mainUserRecycleView.setAdapter(adapter);

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(getApplicationContext(), Register.class));
        }

    }
}