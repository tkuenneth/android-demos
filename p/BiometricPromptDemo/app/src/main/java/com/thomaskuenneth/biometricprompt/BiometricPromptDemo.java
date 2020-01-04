package com.thomaskuenneth.biometricprompt;

import android.app.Activity;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BiometricPromptDemo extends Activity {

    private static final String TAG = BiometricPromptDemo.class.getSimpleName();

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textview);
        BiometricPrompt.Builder b = new BiometricPrompt.Builder(this);
        b.setDescription(getString(R.string.descr));
        b.setTitle(getString(R.string.title));
        b.setSubtitle(getString(R.string.subtitle));
        b.setNegativeButton(getString(R.string.button),
                getMainExecutor(), (dialogInterface, i) ->
                        Log.d(TAG, "button clicked")
        );
        BiometricPrompt d = b.build();
        CancellationSignal cs = new CancellationSignal();
        d.authenticate(cs, getMainExecutor(),
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        toast(R.string.error);
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        toast(R.string.ok);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        toast(R.string.failed);
                    }
                });
    }

    private void toast(int resid) {
        Toast.makeText(this, resid, Toast.LENGTH_LONG).show();
        tv.setText(resid);
    }
}
