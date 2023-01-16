 package com.company.childtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.childtracker.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
   /* private EditText uname,upass;
    private TextView register,forget;
    private Button login;*/

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String emailStr,passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       /* uname=findViewById(R.id.editUserName);
        upass=findViewById(R.id.editPassword);
        register=findViewById(R.id.Register);
        forget=findViewById(R.id.Forget);
        login=findViewById(R.id.Login);*/

        auth=FirebaseAuth.getInstance();

        dialog=new ProgressDialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,Logout.class));
            finish();
        }

        binding.Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });

        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });
        binding.Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgetPassword.class));
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

    private Boolean validatePassword() {
        String val = binding.editPassword.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            binding.editPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            binding.editPassword.setError("Enter Correct Password");
            return false;
        } else {
            binding.editPassword.setError(null);
            return true;
        }
    }

    private void logInUser() {
        if(!validateEmail() || !validatePassword()){
            return;
        }
        else{
            emailStr=binding.editmail.getText().toString();
            passwordStr=binding.editPassword.getText().toString();
            dialog.show();

            logInUsers(emailStr,passwordStr);

        }
    }

    private void logInUsers(String emailStr, String passwordStr) {

        auth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this,Logout.class));
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}
