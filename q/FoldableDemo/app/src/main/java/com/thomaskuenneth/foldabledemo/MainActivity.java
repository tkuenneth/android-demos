package com.thomaskuenneth.foldabledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl;
    private int col = Color.RED;
    private Handler handler = null;
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (handler != null) {
                updateColor();
                handler.postDelayed(runnableCode, 2000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl = findViewById(R.id.frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();
        handler.post(runnableCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler = null;
    }

    private void updateColor() {
        fl.setBackgroundColor(col);
        switch (col) {
            case Color.RED:
                col = Color.GREEN;
                break;
            case Color.GREEN:
                col = Color.YELLOW;
                break;
            default:
                col = Color.RED;
        }
    }
}
