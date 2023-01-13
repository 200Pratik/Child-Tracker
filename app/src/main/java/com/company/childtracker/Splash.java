package com.company.childtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.internal.Util;

public class Splash extends AppCompatActivity {
    private ImageView image;
    private TextView text,text2;
    Animation animationImg,animationTxt,animationTxt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent activityIntent;

        text=findViewById(R.id.textView);
        text2=findViewById(R.id.textView2);
        image=findViewById(R.id.imageView);

        animationImg= AnimationUtils.loadAnimation(this,R.anim.image_animation);
        animationTxt= AnimationUtils.loadAnimation(this,R.anim.text_animation);
        animationTxt2= AnimationUtils.loadAnimation(this,R.anim.text1_animation);

        image.setAnimation(animationImg);
        text.setAnimation(animationTxt);
        text2.setAnimation(animationTxt2);

        new CountDownTimer(5000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(Splash.this,MainActivity.class));


            }
        }.start();









    }
}