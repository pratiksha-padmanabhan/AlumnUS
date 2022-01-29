package com.example.collegeinhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.collegeinhouse.Adapters.ChatsAdapter;
import com.example.collegeinhouse.Models.MessageModel;
import com.example.collegeinhouse.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
ActivityChatDetailBinding binding;
FirebaseDatabase database;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
//final to make it global
        final String senderId=auth.getUid();
        String recieverId=getIntent().getStringExtra("userId");
        String username=getIntent().getStringExtra("username");
        String profilepic=getIntent().getStringExtra("profilepic");
        binding.userNameacd.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.birddd).into(binding.profileImage);


        final ArrayList<MessageModel> messageModels=new ArrayList<>();

        final ChatsAdapter charAdapter= new ChatsAdapter(messageModels,this);
        binding.chatRV.setAdapter(charAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager((this));
        binding.chatRV.setLayoutManager(layoutManager);
        final String senderRoom=senderId+recieverId;
        final String receiverRoom=recieverId+senderId;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();//otherwise it will show the same thing again and afain.only ne ss
            for(DataSnapshot snapshot1:snapshot.getChildren()){
                MessageModel model=snapshot1.getValue(MessageModel.class);
                model.setMessageID(snapshot1.getKey());
                messageModels.add(model);
            }
            charAdapter.notifyDataSetChanged();//runtime
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.senddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= binding.etmessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.etmessage.setText("");
                        //push helps create a new node with timestamp
                database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });

    }
}