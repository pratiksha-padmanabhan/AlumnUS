package com.example.collegeinhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.collegeinhouse.Adapters.ChatsAdapter;
import com.example.collegeinhouse.Models.MessageModel;
import com.example.collegeinhouse.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
ActivityGroupChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
getSupportActionBar().hide();


     final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final String senderId=FirebaseAuth.getInstance().getUid();
    final ChatsAdapter adapter=new ChatsAdapter(messageModels,this);
    binding.chatRV.setAdapter(adapter);
    binding.userNameacd.setText("Group Chat");

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRV.setLayoutManager(layoutManager);

        //to show data on rv
        database.getReference().child("GroupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             messageModels.clear();
             for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                 MessageModel Model=dataSnapshot.getValue(MessageModel.class);
                 messageModels.add(Model);
             }
             adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.senddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=binding.etmessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.etmessage.setText("");

                database.getReference().child("GroupChat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
}