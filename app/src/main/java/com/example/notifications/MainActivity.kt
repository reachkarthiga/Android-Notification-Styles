package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

    companion object {
        val messages: MutableList<Messages> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // For Message Style Notification

        val bharath = Person.Builder().setName("Karthiga")
            .setIcon(IconCompat.createWithResource(applicationContext, R.drawable.user_icon))
            .setKey("xyz@example.com")
            .build();

        val nullPerson: Person? = null

        messages.add(
            Messages(
                "Good Morning",
                bharath
            )
        )

        messages.add(
            Messages(
                "Yes! Tell me .. Happy Morning!",
                nullPerson
            )
        )

        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)

        val notificationChannel = NotificationChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.setShowBadge(true)

        notificationManager?.createNotificationChannel(notificationChannel)

        messageStyle.setOnClickListener {
            notificationManager?.sendNotificationMessageStyle( this)
        }

        inboxStyle.setOnClickListener {
            notificationManager?.sendNotificationInboxStyle(this)
        }

    }
}
