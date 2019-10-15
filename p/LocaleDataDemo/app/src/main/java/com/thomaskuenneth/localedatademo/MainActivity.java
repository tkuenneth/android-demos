package com.thomaskuenneth.localedatademo;

import android.app.Activity;
import android.icu.util.LocaleData;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ULocale locale = ULocale.forLocale(Locale.GERMANY);
        LocaleData ld = LocaleData.getInstance(locale);
        Log.i(TAG, ld.getDelimiter(LocaleData.QUOTATION_START));
        Log.i(TAG, ld.getDelimiter(LocaleData.QUOTATION_END));
        Log.i(TAG, ld.getDelimiter(LocaleData.ALT_QUOTATION_START));
        Log.i(TAG, ld.getDelimiter(LocaleData.ALT_QUOTATION_END));

        LocaleData.PaperSize ps = LocaleData.getPaperSize(locale);
        Log.i(TAG, String.format("width: %d, height: %d",
                ps.getWidth(),
                ps.getHeight()));

        LocaleData.MeasurementSystem ms = LocaleData.getMeasurementSystem(locale);
        if (LocaleData.MeasurementSystem.SI == ms) {
            Log.i(TAG, "SI");
        } else if (LocaleData.MeasurementSystem.UK == ms) {
            Log.i(TAG, "UK");
        } else if (LocaleData.MeasurementSystem.US == ms) {
            Log.i(TAG, "US");
        } else {
            Log.i(TAG, "unknown");
        }
    }
}
