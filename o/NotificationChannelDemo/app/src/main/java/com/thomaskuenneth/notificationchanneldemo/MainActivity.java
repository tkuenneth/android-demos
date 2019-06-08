package com.thomaskuenneth.notificationchanneldemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String CHANNEL_ID = "channel1";

    private static int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NotificationManager nm =
                getSystemService(NotificationManager.class);
        NotificationChannel channel =
                new NotificationChannel(CHANNEL_ID,
                        getString(R.string.channelName),
                        NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(getString(R.string.channelDescr));
        nm.createNotificationChannel(channel);
        Button newNotification = findViewById(R.id.new_notification);
        newNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification notification = new Notification.Builder(MainActivity.this,
                        CHANNEL_ID)
                        .setContentTitle(getString(R.string.contentTitle))
                        .setContentText(getString(R.string.contentText))
                        .setSmallIcon(R.drawable.ic_mail_black_24dp)
                        .build();
                nm.notify(notificationId++, notification);
            }
        });

        Button b = findViewById(R.id.settings);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        });
    }
}
