package com.example.connectwave.Adapter;

import static com.example.connectwave.Activity.ChatAcitivity.rImage;
import static com.example.connectwave.Activity.ChatAcitivity.sImage;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectwave.Model.Messages;
import com.example.connectwave.R;
import com.google.common.collect.ArrayTable;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;

    int item_send = 1;
    int item_recive = 2;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == item_send) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewHolder(view);
        } else {

            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout_item, parent, false);
            return new ReciverViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {

            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        } else {
            ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return item_send;
        } else {
            return item_recive;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);

            txtmessage = itemView.findViewById(R.id.textmessage);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessage;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            txtmessage = itemView.findViewById(R.id.textmessage);
        }
    }
}
