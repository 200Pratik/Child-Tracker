package com.company.childtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.childtracker.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registration extends AppCompatActivity {
    ActivityRegistrationBinding binding;
    //EditText userName, setPass,confirmPass,setemail,mobile;
   // Button submit;
    private String userNameStr, setPassStr,confirmPassStr,setemailStr,mobileStr;
    ProgressDialog dialog;
    private String deviceId;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*userName= findViewById(R.id.editUserName);
        setPass= findViewById(R.id.setPassword);
        confirmPass= findViewById(R.id.confirmPassword);
        mobile=findViewById(R.id.mobile);
        setemail=findViewById(R.id.setemail);
        submit=findViewById(R.id.submit);*/


        deviceId= Settings.Secure.getString(Registration.this.getContentResolver(),Settings.Secure.ANDROID_ID);
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(Registration.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                if(binding.mobile.length()>10){
                    binding.mobile.setError("Mobile No. is Invalid");
                }
            }
        });

    }

    private Boolean validateEmail() {
        String val = binding.setemail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            binding.setemail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            binding.setemail.setError("Invalid email address");
            return false;
        } else {
            binding.setemail.setError(null);
            return true;
        }
    }
    private Boolean validatePhoneNo() {
        String val = binding.mobile.getText().toString();

        if (val.isEmpty()) {
            binding.mobile.setError("Field cannot be empty");
            return false;
        }
        else {
            binding.mobile.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = binding.setPassword.getText().toString();
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
            binding.setPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            binding.setPassword.setError("Password is too weak");
            return false;
        } else {
            binding.setPassword.setError(null);
            return true;
        }
    }
    private Boolean validateConfirmPassword() {
        String val = binding.confirmPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            binding.confirmPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            binding.confirmPassword.setError("Password is too weak");
            return false;
        } else {
            binding.confirmPassword.setError(null);
            return true;
        }
    }

    private  void registerUser(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("UserData");
        final FirebaseUser user=auth.getCurrentUser();

        if(!validateEmail() || !validatePhoneNo() || !validatePassword() || !validateConfirmPassword())
        {
            return;
        }

        userNameStr=binding.editUsername.getEditableText().toString();
        setemailStr=binding.setemail.getEditableText().toString();
        setPassStr=binding.setPassword.getEditableText().toString();
        confirmPassStr=binding.confirmPassword.getEditableText().toString();
        mobileStr=binding.mobile.getEditableText().toString();

        Query query=reference.orderByChild("deviceId").equalTo(deviceId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Toast.makeText(Registration.this,"User Already exist with same Profile",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(validate())
                    {
                        signUpUser(user,userNameStr,setemailStr,mobileStr,setPassStr);
                        dialog.show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void signUpUser(FirebaseUser user, String userNameStr, String setemailStr, String mobileStr, String setPassStr) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("UserData");
        auth.createUserWithEmailAndPassword(setemailStr,setPassStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    String userId=auth.getCurrentUser().getUid();

                    HashMap<String , Object>map=new HashMap<>();

                    map.put("Email",setemailStr);
                    map.put("name",userNameStr);
                    map.put("password",setPassStr);
                    map.put("Mobile",mobileStr);
                    map.put("userId",userId);

                    reference.child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Registration.this,"Registration Successful ",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this,MainActivity.class));
                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }

            }
        });
    }

    private boolean validate() {
        if(setPassStr.compareTo(confirmPassStr)!=0){
            Toast.makeText(Registration.this, "Password Not Matching", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}