package com.example.cp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "timer_channel";

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonStopSound;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;
    private MediaPlayer mediaPlayer;

    private Button mButtonNavigate;
    private ProgressBar mProgressBar;
    private long mInitialTimeInMillis;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mTimeLeftInMillis = intent.getLongExtra("TIMER_VALUE", 0);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonStopSound = findViewById(R.id.button_stop_sound);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        Button mButtonNavigate = findViewById(R.id.button_navigate);
        mInitialTimeInMillis = mTimeLeftInMillis;
        mProgressBar = findViewById(R.id.progress_bar);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        mButtonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerSetUpActivity.class);
                startActivity(intent);
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButtonStopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.prepareAsync(); // prepare for next start
                    mButtonStopSound.setVisibility(View.INVISIBLE);
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }
            }
        });

        createNotificationChannel();
        updateCountDownText();
    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                mButtonStopSound.setVisibility(View.VISIBLE); // Make the "Stop Sound" button visible
                // Get the system service for the vibrator

// Check if the device has a vibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    // Create a long array for your pattern. The values are the number of milliseconds to wait.
                   long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
//                    0: Start immediately
//                    100: Vibrate for 100 milliseconds
//                    1000: Wait for 1000 milliseconds
//                    300: Vibrate for 300 milliseconds
//                    200: Wait for 200 milliseconds
//                    100: Vibrate for 100 milliseconds
//                    500: Wait for 500 milliseconds
//                    200: Vibrate for 200 milliseconds
//                    100: Wait for 100 milliseconds
                    // Create a VibrationEffect object with the pattern and repeat index
                    // The repeat index is the index into the pattern to repeat, or -1 if you don't want to repeat.
                    VibrationEffect vibrationEffect = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrationEffect = VibrationEffect.createWaveform(pattern, -1);
                        vibrator.vibrate(vibrationEffect);
                        }

                    // Vibrate with the created effect

                }



                showNotification();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }
    private void updateProgressBar() {
        int progress = (int) (100 * (mInitialTimeInMillis - mTimeLeftInMillis) / mInitialTimeInMillis);
        mProgressBar.setProgress(progress);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = 0;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            ;
        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < 0) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
            mButtonReset.setVisibility(View.VISIBLE);

        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Timer Channel";
            String description = "Time To Take A Break";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.rest_noti)
                .setContentTitle("Timer Finished")
                .setContentText("Your timer has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build());
    }
}