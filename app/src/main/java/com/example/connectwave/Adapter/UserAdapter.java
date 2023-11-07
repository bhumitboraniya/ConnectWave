package com.example.connectwave.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectwave.Activity.ChatAcitivity;
import com.example.connectwave.Activity.Home;
import com.example.connectwave.Model.users;
import com.example.connectwave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context home;
    ArrayList<users> usersArrayList;
    public UserAdapter(Home home, ArrayList<users> usersArrayList) {
        this.home  = home;
        this.usersArrayList =usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(home).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        users users = usersArrayList.get(position);
        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.imageurl).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(home, ChatAcitivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReciverImage",users.getImageurl());
                intent.putExtra("uid",users.getUid());
                home.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        public ViewHolder(@NonNull View itemView){

            super(itemView);
            user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);

        }
    }
}
