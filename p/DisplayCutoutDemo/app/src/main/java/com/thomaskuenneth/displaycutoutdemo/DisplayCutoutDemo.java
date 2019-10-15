package com.thomaskuenneth.displaycutoutdemo;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class DisplayCutoutDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        window.getAttributes().layoutInDisplayCutoutMode
                = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        setContentView(R.layout.layout);
        final TextView textview = findViewById(R.id.textview);
        final View root = findViewById(R.id.root);
        root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {

            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                StringBuilder sb = new StringBuilder();
                DisplayCutout dc = windowInsets.getDisplayCutout();
                if (dc != null) {
                    sb.append(String.format(Locale.getDefault(),
                            "getSafeInsetLeft(): %d\n", dc.getSafeInsetLeft()));
                    sb.append(String.format(Locale.getDefault(),
                            "getSafeInsetTop(): %d\n", dc.getSafeInsetTop()));
                    sb.append(String.format(Locale.getDefault(),
                            "getSafeInsetRight(): %d\n", dc.getSafeInsetRight()));
                    sb.append(String.format(Locale.getDefault(),
                            "getSafeInsetBottom(): %d\n", dc.getSafeInsetBottom()));
                    List<Rect> l = dc.getBoundingRects();
                    for (int i = 0; i < l.size(); i++) {
                        Rect r = l.get(i);
                        sb.append(String.format(Locale.getDefault(),
                                "Bounding Rect #%d\n", i));
                        sb.append(String.format(Locale.getDefault(),
                                "left: %d, top: %d, right: %d, bottom: %d\n",
                                r.left, r.top, r.right, r.bottom));
                    }
                } else {
                    sb.append("no display cutouts");
                }
                textview.setText(sb.toString());
                return view.onApplyWindowInsets(windowInsets);
            }
        });
    }
}
