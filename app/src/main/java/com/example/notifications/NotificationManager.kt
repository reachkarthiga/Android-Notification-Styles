package com.example.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import com.example.notifications.MainActivity.Companion.messages

const val NOTIFICATION_ID = 1

fun NotificationManager.sendNotificationInboxStyle(context: Context) {

    val builder =
        NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

    val inboxStyle = NotificationCompat.InboxStyle()
        .addLine("Create a GitHub Commit")
        .addLine("Work on App for Play store")
        .addLine("Water the Plants in Garden").setSummaryText("+3 more..")

    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.inbox_style)

    builder.apply {

        setAutoCancel(true)
            .setContentTitle("Daily Routine Reminder")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(ContextCompat.getColor(context, R.color.teal_200))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(bitmap)
            .setStyle(inboxStyle)

    }


    notify(NOTIFICATION_ID, builder.build())

}


fun NotificationManager.sendNotificationMessageStyle(context: Context) {

    val builder =
        NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

    val remoteInput = androidx.core.app.RemoteInput.Builder("text_String").build()
    val intent = Intent(context, ReplyReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val replyAction = NotificationCompat.Action.Builder(
        R.drawable.ic_baseline_notifications_24,
        "REPLY",
        pendingIntent
    )
        .addRemoteInput(remoteInput).build()

    val you = Person.Builder().setName("You")
        .setKey("xyz@example.com")
        .build();

    val messageStyle = NotificationCompat.MessagingStyle(you)

    for (i in messages.indices) {
        messageStyle.addMessage(messages[i].message, messages[i].time, messages[i].user)
    }

    builder.apply {

        setAutoCancel(true)
            .setContentTitle("Inbox Style Notification")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(ContextCompat.getColor(context, R.color.teal_200))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(messageStyle).addAction(replyAction)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

    }

}