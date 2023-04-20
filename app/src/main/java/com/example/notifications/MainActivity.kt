package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.media.session.MediaButtonReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {


    companion object {
        val messages: MutableList<Messages> = mutableListOf()
        lateinit var mediaSession: MediaSessionCompat
    }

    private lateinit var mediaPlayer:MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            addPersonsAndMessages()
            notificationManager?.sendNotificationMessageStyle( this)
        }

        inboxStyle.setOnClickListener {
            notificationManager?.sendNotificationInboxStyle(this)
        }

        mediaStyle.setOnClickListener {
            doMediaThings(applicationContext)
            notificationManager?.sendNotificationMediaStyle(this, mediaSession)
        }

        progressStyle.setOnClickListener {
            notificationManager?.sendNotificationProgressStyle(this)
        }

        bigPictureStyle.setOnClickListener {
            notificationManager?.sendNotificationBigPictureStyle(this)
        }

        customViewStyle.setOnClickListener {
            notificationManager?.sendNotificationCustomViewStyle(this)
        }


    }

    private fun doMediaThings(context:Context) {

        mediaSession =  MediaSessionCompat(context, "Music")

        val state = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0f, SystemClock.elapsedRealtime())
            .build()


        mediaSession.setPlaybackState(state)

        mediaPlayer = MediaPlayer.create(context, R.raw.song)

        val callBack = object : MediaSessionCompat.Callback() {

            var playPosition:Long = 0

            override fun onPlay() {
                mediaPlayer.start()
                mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP)
                    .setState(PlaybackStateCompat.STATE_PLAYING, playPosition , 1f, SystemClock.elapsedRealtime())
                    .build())

            }

            override fun onPause() {
                mediaPlayer.pause()
                playPosition = mediaPlayer.currentPosition.toLong()
                mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP)
                    .setState(PlaybackStateCompat.STATE_PAUSED, playPosition, 0f, SystemClock.elapsedRealtime())
                    .build())
            }

            override fun onStop() {
                mediaPlayer.stop()
                playPosition = 0
                mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_PLAY)
                    .setState(PlaybackStateCompat.STATE_STOPPED, playPosition, 0f, SystemClock.elapsedRealtime())
                    .build())
                mediaPlayer.prepare()
            }

            override fun onSkipToNext() {
                mediaPlayer.stop()
                playPosition = 0
                mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_PLAY)
                    .setState(PlaybackStateCompat.STATE_STOPPED, playPosition, 0f, SystemClock.elapsedRealtime())
                    .build())
                Toast.makeText(context, "Skip Next Clicked" , Toast.LENGTH_SHORT).show()
            }

            override fun onSkipToPrevious() {
                mediaPlayer.stop()
                playPosition = 0
                mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_PLAY)
                    .setState(PlaybackStateCompat.STATE_STOPPED, playPosition, 0f, SystemClock.elapsedRealtime())
                    .build())
                Toast.makeText(context, "Skip Previous Clicked" , Toast.LENGTH_SHORT).show()
            }

        }

        mediaSession.setCallback(callBack)

        mediaPlayer.setOnCompletionListener {
            Toast.makeText(
                context,
                "Player completed",
                Toast.LENGTH_SHORT
            ).show()
        }

        mediaSession.isActive = true

    }

    private fun addPersonsAndMessages() {
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

    }

    class MediaPlayReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            MediaButtonReceiver.handleIntent(mediaSession, intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
        mediaPlayer.release()
    }

}

