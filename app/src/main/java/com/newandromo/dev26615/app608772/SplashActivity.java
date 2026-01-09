// SplashActivity.java - Splash Screen
package com.newandromo.dev26615.app608772;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Animate decorative blocks
        View block1 = findViewById(R.id.splashBlock1);
        View block2 = findViewById(R.id.splashBlock2);

        // Falling animation for block 1
        ObjectAnimator fall1 = ObjectAnimator.ofFloat(block1, "translationY", -200f, 0f);
        fall1.setDuration(1000);
        fall1.setInterpolator(new AccelerateDecelerateInterpolator());
        fall1.start();

        // Falling animation for block 2 with delay
        ObjectAnimator fall2 = ObjectAnimator.ofFloat(block2, "translationY", -200f, 0f);
        fall2.setDuration(1000);
        fall2.setStartDelay(200);
        fall2.setInterpolator(new AccelerateDecelerateInterpolator());
        fall2.start();

        // Rotate while falling
        ObjectAnimator rotate1 = ObjectAnimator.ofFloat(block1, "rotation", 20f, 380f);
        rotate1.setDuration(1000);
        rotate1.start();

        ObjectAnimator rotate2 = ObjectAnimator.ofFloat(block2, "rotation", -15f, -375f);
        rotate2.setDuration(1000);
        rotate2.setStartDelay(200);
        rotate2.start();

        // Navigate after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}