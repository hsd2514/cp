package com.example.cp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class TimerSetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_set_up);

        final EditText editTextTimer = findViewById(R.id.edit_text_timer);
        Button mButtonSetTimer = findViewById(R.id.button_set_timer);
        Button mButtonWaterBreak = findViewById(R.id.button_water_break);
        Button mButtonTakeWalk = findViewById(R.id.button_take_walk);

        mButtonSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timerText = editTextTimer.getText().toString();
                int minutes = Integer.parseInt(timerText);
                long timerValue = minutes * 60 * 1000;

                Intent intent = new Intent(TimerSetUpActivity.this, MainActivity.class);
                intent.putExtra("TIMER_VALUE", timerValue);
                startActivity(intent);
            }
        });

        mButtonWaterBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timerValue = 20 * 60 * 1000; // 20 minutes

                Intent intent = new Intent(TimerSetUpActivity.this, MainActivity.class);
                intent.putExtra("TIMER_VALUE", timerValue);
                startActivity(intent);
            }
        });

        mButtonTakeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timerValue = 30 * 60 * 1000; // 30 minutes

                Intent intent = new Intent(TimerSetUpActivity.this, MainActivity.class);
                intent.putExtra("TIMER_VALUE", timerValue);
                startActivity(intent);
            }
        });
    }
}