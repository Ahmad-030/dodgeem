// HighScoreActivity.java - High Scores Screen
package com.newandromo.dev26615.app608772;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HighScoreActivity extends AppCompatActivity {
    private LinearLayout scoreContainer;
    private Button btnBack, btnClearScores;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scoreContainer = findViewById(R.id.scoreContainer);
        btnBack = findViewById(R.id.btnBackFromScores);
        btnClearScores = findViewById(R.id.btnClearScores);
        prefs = getSharedPreferences("DodgeEmPrefs", Context.MODE_PRIVATE);

        displayHighScores();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnClearScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("highScores", "");
                editor.apply();
                displayHighScores();
            }
        });
    }

    private void displayHighScores() {
        scoreContainer.removeAllViews();

        String scoresString = prefs.getString("highScores", "");

        if (scoresString.isEmpty()) {
            TextView noScores = new TextView(this);
            noScores.setText("No high scores yet!\nPlay a game to set a record.");
            noScores.setTextSize(20);
            noScores.setTextColor(0xFFFFFFFF);
            noScores.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noScores.setPadding(0, 100, 0, 0);
            scoreContainer.addView(noScores);
            return;
        }

        String[] scoreEntries = scoresString.split(";");
        ArrayList<ScoreEntry> scores = new ArrayList<>();

        for (String entry : scoreEntries) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                try {
                    int score = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    scores.add(new ScoreEntry(score, timestamp));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry s1, ScoreEntry s2) {
                return Integer.compare(s2.score, s1.score);
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        int displayCount = Math.min(10, scores.size());
        for (int i = 0; i < displayCount; i++) {
            ScoreEntry entry = scores.get(i);

            View scoreView = getLayoutInflater().inflate(R.layout.score_item, null);
            TextView tvRank = scoreView.findViewById(R.id.tvRank);
            TextView tvScore = scoreView.findViewById(R.id.tvScore);
            TextView tvDate = scoreView.findViewById(R.id.tvDate);

            tvRank.setText("#" + (i + 1));
            tvScore.setText(entry.score + " seconds");
            tvDate.setText(sdf.format(new Date(entry.timestamp)));

            if (i == 0) {
                scoreView.setBackgroundColor(0x44FFD700);
            }

            scoreContainer.addView(scoreView);
        }
    }

    class ScoreEntry {
        int score;
        long timestamp;

        ScoreEntry(int score, long timestamp) {
            this.score = score;
            this.timestamp = timestamp;
        }
    }
}