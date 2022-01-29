package com.example.collegeinhouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.collegeinhouse.Models.Users;
import com.example.collegeinhouse.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
ActivitySettingsBinding binding;
FirebaseAuth auth;
FirebaseStorage storage;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
getSupportActionBar().hide();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction((Intent.ACTION_GET_CONTENT));
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

        binding.btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=binding.etuname.getText().toString();
                String colname=binding.etcol.getText().toString();
                String jobname=binding.etjob.getText().toString();

                //hash map to updte value in firebase
                HashMap<String,Object> obj=new HashMap<>();
                obj.put("userName",username);
                obj.put("college",colname);
                obj.put("job",jobname);
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                    Toast.makeText(SettingsActivity.this,"Profile updated!",Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.birddd).into(binding.profilepic);

                binding.etuname.setText(users.getUserName());
                binding.etjob.setText(users.getJob());
                binding.etcol.setText(users.getCollege());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data.getData()!=null){
            Uri uri=data.getData();
            binding.profilepic.setImageURI(uri);

            final StorageReference reference=storage.getReference().child("profile_pic").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child((FirebaseAuth.getInstance().getUid())).child("profilepic").setValue(uri.toString());
                        }
                    });
                   // Toast.makeText(SettingsActivity.this,"Uploaded photo",Toast.LENGTH_LONG).show();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}