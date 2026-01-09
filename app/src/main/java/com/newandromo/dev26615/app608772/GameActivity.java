// GameActivity.java - Main Game Screen
package com.newandromo.dev26615.app608772;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    class GameView extends SurfaceView implements Runnable {
        private Thread gameThread;
        private SurfaceHolder holder;
        private boolean isPlaying;
        private Paint paint;
        private Random random;

        private float playerX, playerY;
        private float playerSize = 80;
        private float playerSpeed = 20;
        private boolean moveLeft = false;
        private boolean moveRight = false;

        private ArrayList<Block> blocks;
        private long startTime;
        private int score;
        private SharedPreferences prefs;

        private int screenWidth, screenHeight;
        private float blockSpeed = 8;
        private long lastBlockTime;
        private int blockSpawnDelay = 1000;
        private boolean gameOver = false;

        public GameView(Context context) {
            super(context);
            holder = getHolder();
            paint = new Paint();
            random = new Random();
            blocks = new ArrayList<>();
            prefs = context.getSharedPreferences("DodgeEmPrefs", Context.MODE_PRIVATE);
            setFocusable(true);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w;
            screenHeight = h;
            playerX = screenWidth / 2 - playerSize / 2;
            playerY = screenHeight - playerSize - 100;
            startGame();
        }

        private void startGame() {
            blocks.clear();
            startTime = System.currentTimeMillis();
            lastBlockTime = System.currentTimeMillis();
            score = 0;
            gameOver = false;
            blockSpeed = 8;
            blockSpawnDelay = 1000;
        }

        @Override
        public void run() {
            while (isPlaying) {
                update();
                draw();
                sleep();
            }
        }

        private void update() {
            if (gameOver) return;

            // Update player position
            if (moveLeft) {
                playerX -= playerSpeed;
                if (playerX < 0) playerX = 0;
            }
            if (moveRight) {
                playerX += playerSpeed;
                if (playerX > screenWidth - playerSize) playerX = screenWidth - playerSize;
            }

            long currentTime = System.currentTimeMillis();
            score = (int)((currentTime - startTime) / 100);

            // Increase difficulty
            blockSpeed = 8 + score / 50f;
            blockSpawnDelay = Math.max(300, 1000 - score * 3);

            // Spawn blocks
            if (currentTime - lastBlockTime > blockSpawnDelay) {
                float blockWidth = 60 + random.nextInt(100);
                float maxX = screenWidth - blockWidth;
                float blockX = maxX > 0 ? random.nextInt((int)maxX) : 0;
                int colorType = random.nextInt(4);
                blocks.add(new Block(blockX, -100, blockWidth, 60, colorType));
                lastBlockTime = currentTime;
            }

            // Update blocks
            for (int i = blocks.size() - 1; i >= 0; i--) {
                Block block = blocks.get(i);
                block.y += blockSpeed;

                if (block.y > screenHeight) {
                    blocks.remove(i);
                    continue;
                }

                // Collision detection
                if (block.x < playerX + playerSize &&
                        block.x + block.width > playerX &&
                        block.y < playerY + playerSize &&
                        block.y + block.height > playerY) {
                    gameOver = true;
                    saveScore();
                }
            }
        }

        private void saveScore() {
            String scoresString = prefs.getString("highScores", "");
            String newScore = score + "," + System.currentTimeMillis();

            if (scoresString.isEmpty()) {
                scoresString = newScore;
            } else {
                scoresString = newScore + ";" + scoresString;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("highScores", scoresString);
            editor.apply();
        }

        private void draw() {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();

                // Background gradient
                canvas.drawColor(Color.rgb(20, 20, 50));

                // Draw blocks with different colors
                for (Block block : blocks) {
                    switch(block.colorType) {
                        case 0: paint.setColor(Color.RED); break;
                        case 1: paint.setColor(Color.BLUE); break;
                        case 2: paint.setColor(Color.MAGENTA); break;
                        case 3: paint.setColor(Color.YELLOW); break;
                    }
                    canvas.drawRect(block.x, block.y, block.x + block.width,
                            block.y + block.height, paint);
                }

                // Draw player
                paint.setColor(Color.rgb(0, 255, 100));
                canvas.drawRect(playerX, playerY, playerX + playerSize,
                        playerY + playerSize, paint);

                // Draw score
                paint.setColor(Color.WHITE);
                paint.setTextSize(50);
                canvas.drawText("Score: " + score, 40, 70, paint);

                // Game over overlay
                if (gameOver) {
                    paint.setColor(Color.argb(180, 0, 0, 0));
                    canvas.drawRect(0, 0, screenWidth, screenHeight, paint);

                    paint.setTextSize(100);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(Color.RED);
                    canvas.drawText("GAME OVER", screenWidth / 2, screenHeight / 2 - 100, paint);

                    paint.setTextSize(50);
                    paint.setColor(Color.WHITE);
                    canvas.drawText("Your Score: " + score, screenWidth / 2, screenHeight / 2, paint);
                    canvas.drawText("Tap to Continue", screenWidth / 2, screenHeight / 2 + 100, paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                }

                holder.unlockCanvasAndPost(canvas);
            }
        }

        private void sleep() {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resume() {
            isPlaying = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void pause() {
            try {
                isPlaying = false;
                if (gameThread != null) {
                    gameThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (gameOver) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
                return true;
            }

            float touchX = event.getX();

            if (event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_MOVE) {

                if (touchX < screenWidth / 3) {
                    moveLeft = true;
                    moveRight = false;
                } else if (touchX > 2 * screenWidth / 3) {
                    moveRight = true;
                    moveLeft = false;
                } else {
                    playerX = touchX - playerSize / 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                moveLeft = false;
                moveRight = false;
            }

            return true;
        }

        class Block {
            float x, y, width, height;
            int colorType;

            Block(float x, float y, float width, float height, int colorType) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.colorType = colorType;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameView.pause();
        finish();
    }
}