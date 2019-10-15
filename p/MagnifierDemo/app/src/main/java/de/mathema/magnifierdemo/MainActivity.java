package de.mathema.magnifierdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Magnifier;

/**
 * Diese Activity zeigt die Verwendung der Klasse
 * android.widget.Magnifier
 *
 * @see <a href="https://developer.android.com/reference/android/widget/Magnifier.html" />
 */
public class MainActivity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup layout = findViewById(R.id.layout);
        Magnifier magnifier = new Magnifier(layout);
        layout.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (handler == null) {
                        handler = new Handler();
                    }
                    magnifier.show(motionEvent.getX(), motionEvent.getY());
                    post(handler, magnifier);
                    return true;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    if (handler != null) {
                        handler = null;
                    }
                    magnifier.dismiss();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void post(Handler handler, Magnifier magnifier) {
        handler.postDelayed(() -> {
            magnifier.update();
            post(handler, magnifier);
        }, 200);
    }
}
