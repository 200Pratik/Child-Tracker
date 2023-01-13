package com.company.childtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.company.childtracker.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {
    ActivityLogoutBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(Logout.this,MainActivity.class));
                finish();
            }
        });
    }
}