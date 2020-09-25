package com.thomaskuenneth.bubbledemo

import android.app.*
import android.content.Intent
import android.content.LocusId
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val CHANNEL_ID = "BubbleDemo"
private const val NOTIFICATION_ID = 123
private const val LOCUS_ID = "ABC"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val icon = Icon.createWithResource(this, R.drawable.ic_baseline_bubble_chart_24)
        val person = Person.Builder()
                .setName(getString(R.string.partner))
                .setImportant(true)
                .setIcon(icon)
                .build()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ix.de"))
        val shortcut = ShortcutInfo.Builder(this, "id1")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                .setIntent(intent)
                .setPerson(person)
                .setLocusId(LocusId(LOCUS_ID))
                .setLongLived(true)
                .build()
        getSystemService(ShortcutManager::class.java)?.run {
            pushDynamicShortcut(shortcut)
        }

        val target = Intent(this, BubbleActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(this, 0, target, 0)
        val builder = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .addPerson(person)
                .setShortcutId(shortcut.id)
                .setCategory(Notification.CATEGORY_CALL)
                .setStyle(Notification.MessagingStyle(person))
                .setContentIntent(bubbleIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)

        val bubbleData = Notification.BubbleMetadata.Builder(bubbleIntent, icon)
                .setDesiredHeight(600)
                .build()
        builder.setBubbleMetadata(bubbleData)

        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
        manager.notify(NOTIFICATION_ID, builder.build())
        finish()
    }
}