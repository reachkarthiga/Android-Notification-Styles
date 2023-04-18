package com.example.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
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

    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.sendNotificationMediaStyle(
    context: Context,
    mediaSession: MediaSessionCompat
) {

    val builder =
        NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

    val mediaStyle =
        androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1,3,4)

    mediaStyle.setMediaSession(mediaSession.sessionToken)

    builder.apply {

        setAutoCancel(true)
            .setContentTitle("Playing..")
            .setContentText("Aga Naga from PS - 2")
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ps2))
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(ContextCompat.getColor(context, R.color.white))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setStyle(mediaStyle)
            .addAction(
                R.drawable.ic_baseline_skip_previous_24,
                "PREVIOUS",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            ) .addAction(
                R.drawable.ic_baseline_play_arrow_24,
                "PLAY",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY
                )

            )
            .addAction(
                R.drawable.ic_baseline_skip_next_24,
                "NEXT", MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
            .addAction(
                R.drawable.ic_baseline_pause_24,
                "PAUSE",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PAUSE
                )
            )

            .addAction(
                R.drawable.ic_baseline_stop_24,
                "STOP",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            ).setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
    }

    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.sendNotificationProgressStyle(context: Context) {

    val builder =
        NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

    builder.apply {

        setAutoCancel(true)
            .setContentTitle("Progress Style Notification")
            .setContentText("Downloading..")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.download_icon))
            .setColor(ContextCompat.getColor(context, R.color.teal_200))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(0, 0, true)
    }

    notify(NOTIFICATION_ID, builder.build())

//    for (i in 0..100) {
//        Thread.sleep(100)
//        builder.setPriority(NotificationCompat.PRIORITY_MIN)
//        builder.setProgress(100, i, false)
//        notify(NOTIFICATION_ID, builder.build())
//    }

    Thread.sleep(5000)

    builder.setProgress(0, 0, false)
        .setContentText("Downloaded")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, builder.build())


}

fun NotificationManager.sendNotificationBigPictureStyle(context: Context) {

    val builder =
        NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(BitmapFactory.decodeResource(context.resources, R.drawable.sales_large_icon))

    builder.apply {

        setAutoCancel(true)
            .setContentTitle("Mobile Sales").setContentText("Amazon Mobile Sales is now live!!")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(ContextCompat.getColor(context, R.color.teal_200))
            .setStyle(bigPictureStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    notify(NOTIFICATION_ID, builder.build())


}