package com.company.childtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.company.childtracker.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

        dialog=new ProgressDialog(ForgetPassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        binding.Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword();
            }
        });
    }

    private Boolean validateEmail() {
        String val = binding.editmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            binding.editmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            binding.editmail.setError("Invalid email address");
            return false;
        } else {
            binding.editmail.setError(null);
            return true;
        }
    }

    private void forgetPassword() {
        if(!validateEmail()){
            return;
        }
        dialog.show();

        auth.sendPasswordResetEmail(binding.editmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                    finish();
                    Toast.makeText(ForgetPassword.this, "Please Check Your E-mail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForgetPassword.this, "Enter Correct E-mail", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}