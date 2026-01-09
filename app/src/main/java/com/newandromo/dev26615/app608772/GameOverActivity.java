// GameOverActivity.java - Game Over Screen
package com.newandromo.dev26615.app608772;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private TextView tvGameOverTitle, tvFinalScore;
    private Button btnRestart, btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        tvGameOverTitle = findViewById(R.id.tvGameOverTitle);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnRestart = findViewById(R.id.btnRestart);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        int score = getIntent().getIntExtra("score", 0);
        tvFinalScore.setText("Your Score: " + score + " seconds");

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, GetStartedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GameOverActivity.this, GetStartedActivity.class);
        startActivity(intent);
        finish();
    }
}