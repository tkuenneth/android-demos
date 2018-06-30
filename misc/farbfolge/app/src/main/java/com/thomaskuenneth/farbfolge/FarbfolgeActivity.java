package com.thomaskuenneth.farbfolge;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


class FarbfolgeActivity extends Activity implements View.OnTouchListener {

    private static final String TAG = FarbfolgeActivity.class.getSimpleName();
    private static final String STR_REKORD = "rekord";

    private static final long VERZOEGERUNG_VOR_HELL = 600;
    private static final long DAUER_HELL = 700;

    private enum Status {SPIEL_NICHT_GESTARTET, FARBFOLGE_ANZEIGEN, EINGABE_ABWARTEN}

    // High score
    private TextView rekord;

    // Spielstart
    private Button start;

    // Farb-Buttons
    private Button rot, gelb, gruen, blau;

    // Spieldaten
    private static final int ANZAHL_FARBEN = 8;
    private Farbe[] zufaelligeFarben = new Farbe[ANZAHL_FARBEN];
    private int richtigGerateneFarben;
    private int aktuelleRateposition;
    private Status status;
    private Timer timer;

    long zuletztGestartet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farbfolge_layout);

        rekord = (TextView) findViewById(R.id.rekord);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spielStarten();
            }
        });

        rot = (Button) findViewById(R.id.rot);
        rot.setTag(Farbe.ROT);
        rot.setOnTouchListener(this);

        gelb = (Button) findViewById(R.id.gelb);
        gelb.setTag(Farbe.GELB);
        gelb.setOnTouchListener(this);

        gruen = (Button) findViewById(R.id.gruen);
        gruen.setTag(Farbe.GRUEN);
        gruen.setOnTouchListener(this);

        blau = (Button) findViewById(R.id.blau);
        blau.setTag(Farbe.BLAU);
        blau.setOnTouchListener(this);

        spielVorbereiten();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (status == Status.EINGABE_ABWARTEN) {
            Button button = (Button) v;
            Farbe f = (Farbe) button.getTag();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setzeFarbe(button, true);
                    timerStoppen();
                    break;
                case MotionEvent.ACTION_UP:
                    setzeFarbe(button, false);
                    rateFarbe(f);
                    break;
            }
        }
        return false;
    }

    private void spielVorbereiten() {
        status = Status.SPIEL_NICHT_GESTARTET;
        setzeFarbe(rot, false);
        setzeFarbe(gelb, false);
        setzeFarbe(gruen, false);
        setzeFarbe(blau, false);
        start.setEnabled(true);
        timer = null;
        // Rekord ermitteln
        SharedPreferences p = getSharedPreferences(TAG, MODE_PRIVATE);
        long r = p.getLong(STR_REKORD, 0);
        if (r != 0) {
            rekord.setText(getString(R.string.muster, (r + 999) / 1000));
        } else {
            rekord.setText(R.string.willkommen);
        }
    }

    private void spielStarten() {
        // zufällige Farbfolge ermitteln
        final Farbe[] farbmapping = new Farbe[]{Farbe.ROT, Farbe.GELB, Farbe.GRUEN, Farbe.BLAU};
        Random r = new Random();
        for (int i = 0; i < zufaelligeFarben.length; i++) {
            zufaelligeFarben[i] = farbmapping[r.nextInt(4)];
        }
        // Positionszähler initialisieren und die erste Farbe anzeigen
        richtigGerateneFarben = 0;
        aktuelleRateposition = 0;
        start.setEnabled(false);
        zuletztGestartet = System.currentTimeMillis();
        farbeAnzeigen(0);
    }

    private void farbeAnzeigen(final int index) {
        if (index == 0) {
            status = Status.FARBFOLGE_ANZEIGEN;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setzeFarbe(zufaelligeFarben[index], true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setzeFarbe(zufaelligeFarben[index], false);
                        final int idx = 1 + index;
                        if (idx <= richtigGerateneFarben) {
                            farbeAnzeigen(idx);
                        } else {
                            status = Status.EINGABE_ABWARTEN;
                            timerStarten();
                        }
                    }
                }, DAUER_HELL);
            }
        }, VERZOEGERUNG_VOR_HELL);
    }

    private void setzeFarbe(Button button, boolean hell) {
        Farbe farbe = (Farbe) button.getTag();
        int f = hell ? farbe.getFarbeHell() : farbe.getFarbeDunkel();
        button.setBackgroundColor(f);
    }

    private void setzeFarbe(Farbe farbe, boolean hell) {
        switch (farbe) {
            case ROT:
                setzeFarbe(rot, hell);
                break;
            case GELB:
                setzeFarbe(gelb, hell);
                break;
            case GRUEN:
                setzeFarbe(gruen, hell);
                break;
            case BLAU:
                setzeFarbe(blau, hell);
                break;
        }
    }

    private void rateFarbe(Farbe farbe) {
        if (zufaelligeFarben[aktuelleRateposition].equals(farbe)) {
            if (aktuelleRateposition == richtigGerateneFarben) {
                farbfolgeVerlaengern();
            } else {
                aktuelleRateposition += 1;
                timerStarten();
            }
        } else {
            verloren();
        }
    }

    private void farbfolgeVerlaengern() {
        richtigGerateneFarben += 1;
        if (richtigGerateneFarben == zufaelligeFarben.length) {
            gewonnen();
        } else {
            aktuelleRateposition = 0;
            farbeAnzeigen(0);
        }
    }

    private void verloren() {
        Toast.makeText(this, R.string.verloren, Toast.LENGTH_LONG).show();
        spielVorbereiten();
    }

    private void gewonnen() {
        Toast.makeText(this, R.string.gewonnen, Toast.LENGTH_LONG).show();
        SharedPreferences p = getSharedPreferences(TAG, MODE_PRIVATE);
        long spieldauer = System.currentTimeMillis() - zuletztGestartet;
        long rekord = p.getLong(STR_REKORD, Long.MAX_VALUE);
        if (spieldauer < rekord) {
            p.edit().putLong(STR_REKORD, spieldauer).apply();
        }
        spielVorbereiten();
    }

    private void timerStarten() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        verloren();
                    }
                });
            }
        }, 3000L);
    }

    private void timerStoppen() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
