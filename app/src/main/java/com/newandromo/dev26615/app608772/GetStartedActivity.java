// GetStartedActivity.java - Main Menu
package com.newandromo.dev26615.app608772;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {
    private Button btnStartGame, btnHighScores, btnAbout;
    private View block1, block2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btnStartGame = findViewById(R.id.btnStartGame);
        btnHighScores = findViewById(R.id.btnHighScores);
        btnAbout = findViewById(R.id.btnAbout);
        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);

        // Animate decorative blocks
        animateBlocks();

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
    }

    private void animateBlocks() {
        // Animate block 1 - floating up and down
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(block1, "translationY", 0f, -30f, 0f);
        anim1.setDuration(3000);
        anim1.setRepeatCount(ValueAnimator.INFINITE);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.start();

        // Animate block 2 - floating up and down with delay
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(block2, "translationY", 0f, -25f, 0f);
        anim2.setDuration(2500);
        anim2.setStartDelay(500);
        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2.start();

        // Rotate animation for block 1
        ObjectAnimator rotate1 = ObjectAnimator.ofFloat(block1, "rotation", 15f, 25f, 15f);
        rotate1.setDuration(4000);
        rotate1.setRepeatCount(ValueAnimator.INFINITE);
        rotate1.setInterpolator(new AccelerateDecelerateInterpolator());
        rotate1.start();

        // Rotate animation for block 2
        ObjectAnimator rotate2 = ObjectAnimator.ofFloat(block2, "rotation", -10f, -20f, -10f);
        rotate2.setDuration(3500);
        rotate2.setRepeatCount(ValueAnimator.INFINITE);
        rotate2.setInterpolator(new AccelerateDecelerateInterpolator());
        rotate2.start();
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);

        Button btnClose = dialog.findViewById(R.id.btnCloseAbout);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}