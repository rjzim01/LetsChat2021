package com.rjzim.letschat2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rjzim.letschat2021.Models.Users;
import com.rjzim.letschat2021.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while creating account!!!");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.signUpEmail.getText().toString().isEmpty()){
                    binding.signUpEmail.setError("Please enter Your email !!");
                    return;
                }

                if (binding.signupPassword.getText().toString().isEmpty()){
                    binding.signupPassword.setError("Please enter Your password !!");
                    return;
                }

                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.signUpEmail.getText().toString(),binding.signupPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Users users = new Users(binding.signUpName.getText().toString() , binding.signUpEmail.getText().toString() , binding.signupPassword.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(users);

                            Intent intent = new Intent(SignUpActivity.this , MainActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();


                        }
                        else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        binding.signupHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this , SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}