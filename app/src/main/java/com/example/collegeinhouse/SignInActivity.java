package com.example.collegeinhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.collegeinhouse.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
ActivitySignInBinding binding;
FirebaseAuth auth;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Signing you in!");
        progressDialog.setMessage("Welcome Back!");
        auth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                    if(binding.etEmail.getText().toString().isEmpty()){
                        binding.etEmail.setError("Enter your email");
                        return;
                    }
                if(binding.etpassword.getText().toString().isEmpty()){
                    binding.etpassword.setError("Enter your password");
                    return;
                }
                auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            //Toast.makeText(SignInActivity.this,"hey3",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            //Toast.makeText(SignInActivity.this,"hey4",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                            else
                            {
                                Toast.makeText(SignInActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();


                            }
                        }

                });
            }
        });
        binding.tvclicksignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}