package com.example.cp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TimerSetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_set_up);


        Button mButtonSet10Sec = findViewById(R.id.button_set_10_sec);
        mButtonSet10Sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerSetUpActivity.this, MainActivity.class);
                long timerValue = 10000;
                intent.putExtra("TIMER_VALUE", timerValue);

                startActivity(intent);
            }
        });
}
}