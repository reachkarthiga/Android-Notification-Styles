package com.example.notifications

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver

class ReplyReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val bundle = RemoteInput.getResultsFromIntent(intent)
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)

        if (bundle != null) {
            val reply = bundle.getCharSequence("text_String")
            MainActivity.messages.add(Messages(reply.toString(), null))
            notificationManager?.sendNotificationMessageStyle(context)
        }

    }
    
}

