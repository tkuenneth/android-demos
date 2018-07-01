package com.thomaskuenneth.sharedmemorydemo;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.ByteBuffer;

import de.mathema.sharedmemorydemo.R;

import static android.system.OsConstants.PROT_READ;
import static android.system.OsConstants.PROT_WRITE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_BUNDLE = "bundle";
    private static final String EXTRA_BINDER = "shm";

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.inputOutput);
        Button b = findViewById(R.id.submit);
        et.setOnEditorActionListener((textView, i, keyEvent) -> {
            b.performClick();
            return true;
        });
        b.setOnClickListener((view) -> {
            String text = et.getText().toString();
            try {
                SharedMemory shm = SharedMemory.create("my shared memory", 512);
                shm.setProtect(PROT_READ | PROT_WRITE);
                ByteBuffer bb = shm.mapReadWrite();
                bb.putInt(text.length());
                bb.put(text.getBytes());
                shm.setProtect(PROT_READ);

                Intent intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                MyBinder binder = new MyBinder();
                binder.shm = shm;
                bundle.putBinder(EXTRA_BINDER, binder);
                intent.putExtra(EXTRA_BUNDLE, bundle);
                startActivity(intent);

            } catch (ErrnoException e) {
                Log.e(TAG, null, e);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        et.setText("");
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            if (bundle != null) {
                MyBinder binder = (MyBinder) bundle.getBinder(EXTRA_BINDER);
                if (binder != null) {
                    if (binder.shm != null) {
                        try {
                            ByteBuffer bb = binder.shm.mapReadOnly();
                            int len = bb.getInt();
                            byte[] desti = new byte[len];
                            bb.get(desti);
                            String text = new String(desti);
                            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                            SharedMemory.unmap(bb);
                            binder.shm.close();
                        } catch (ErrnoException e) {
                            Log.e(TAG, null, e);
                        }
                    }
                }
            }
        }
    }

    static class MyBinder extends Binder {
        SharedMemory shm;
    }
}
