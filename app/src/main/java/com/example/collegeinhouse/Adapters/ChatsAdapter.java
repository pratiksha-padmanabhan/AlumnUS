package com.example.collegeinhouse.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeinhouse.Models.MessageModel;
import com.example.collegeinhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter{
ArrayList<MessageModel> messageModels;
Context context;
String receiverID;
int senderViewType=1;
int receiverViewType=2;


    public ChatsAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatsAdapter(ArrayList<MessageModel> messageModels, Context context, String receiverID) {
        this.messageModels = messageModels;
        this.context = context;
        this.receiverID = receiverID;
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==senderViewType){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return senderViewType;
        }
        else
            return receiverViewType;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    MessageModel messageModel=messageModels.get(position);
    //when user holds the message for a while
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete this message? ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        String senderr=FirebaseAuth.getInstance().getUid()+receiverID;
                        database.getReference().child("Chats").child(senderr).child(messageModel.getMessageID()).setValue(null);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });
    // senders message stored in firebase. to get that message on receivers side too
    if(holder.getClass()==SenderViewHolder.class){
        ((SenderViewHolder)holder).senderMessage.setText(messageModel.getMessage());
    }
    else
        ((ReceiverViewHolder)holder).receiverMessage.setText(messageModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage=itemView.findViewById(R.id.recieverText);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);
        }
    }
}
