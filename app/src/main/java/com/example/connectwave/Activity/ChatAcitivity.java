package com.example.connectwave.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectwave.Adapter.MessageAdapter;
import com.example.connectwave.Model.Messages;
import com.example.connectwave.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAcitivity extends AppCompatActivity {

    String ReciverImage, ReciverUID, ReciverName, SenderUid;
    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;
    CardView sendbtn;
    EditText editmsg;
    String senderRoom, reciverRoom;
    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessageAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acitivity);
        ReciverName = getIntent().getStringExtra("name");
        ReciverImage = getIntent().getStringExtra("ReciverImage");
        ReciverUID = getIntent().getStringExtra("uid");
        messagesArrayList = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(getApplicationContext(),messagesArrayList);
        messageAdapter.setAdapter(adapter);

        sendbtn = findViewById(R.id.sendbtn);
        editmsg = findViewById(R.id.edit_message);


        profileImage = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciverName);

        Picasso.get().load(ReciverImage).into(profileImage);
        reciverName.setText(""+ReciverName);

            SenderUid = firebaseAuth.getUid();

            senderRoom = SenderUid + ReciverUID;
            reciverRoom = ReciverUID + senderRoom;


            DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
            DatabaseReference chatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");

            chatReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesArrayList.add(messages);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sImage = snapshot.child("imageurl").getValue().toString();
                    rImage = ReciverImage;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            sendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = editmsg.getText().toString();
                    if (message.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "please enter a message", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editmsg.setText("");
                    Date date = new Date();
                    Messages messages = new Messages(message, SenderUid, date.getTime());
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages").push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference().child("chats")
                                            .child(reciverRoom)
                                            .child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            });

    }
}