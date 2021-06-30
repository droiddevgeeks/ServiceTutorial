package com.example.servicedemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MyForeGroundService : Service() {

    companion object {
        private val TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE"
        val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        val ACTION_PAUSE = "ACTION_PAUSE"
        private val ACTION_PLAY = "ACTION_PLAY"
        private val CHANNEL_ID = "my_service_123"
        private val CHANNEL_NAME = "My Background Service"
        private val NOTIFICATION_ID = 1
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().")
    }

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { mIntent ->
            val action = mIntent.action
            action?.let { mAction ->
                when (mAction) {
                    ACTION_START_FOREGROUND_SERVICE -> {
                        startForegroundService()
                    }
                    ACTION_STOP_FOREGROUND_SERVICE -> {
                        stopForegroundService()
                    }
                    ACTION_PLAY -> {
                        Toast.makeText(
                            applicationContext,
                            "You click Play button.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    ACTION_PAUSE -> {
                        Toast.makeText(
                            applicationContext,
                            "You click Pause button",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        Log.v(TAG_FOREGROUND_SERVICE, "Start foreground service.")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChannel()
        else createNotificationBelowOreo()
    }

    private fun createNotificationBelowOreo() {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 123, intent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.")
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.")
        builder.setStyle(bigTextStyle)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)

        val largeIconBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
        builder.setLargeIcon(largeIconBitmap)
        builder.priority = Notification.PRIORITY_MAX

        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)

        // Add Play button intent in notification.
        val playIntent = Intent(this, MyForeGroundService::class.java)
        playIntent.action = ACTION_PLAY
        val pendingPlayIntent = PendingIntent.getService(this, 123, playIntent, 0)
        val playAction =
            NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent)
        builder.addAction(playAction)

        // Add Pause button intent in notification.
        val pauseIntent = Intent(this, MyForeGroundService::class.java)
        pauseIntent.action = ACTION_PAUSE
        val pendingPrevIntent = PendingIntent.getService(this, 123, pauseIntent, 0)
        val prevAction =
            NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent)
        builder.addAction(prevAction)

        val notification = builder.build()
        startForeground(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationClickIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationClickIntent, 0)

        val chan =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.")
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.")
        bigTextStyle.setSummaryText("By: Kishan")

        val largeIconBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)

        // Assign big picture notification
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .build()

        //Implement inbox style notification
        val iStyle = NotificationCompat.InboxStyle()
        iStyle.addLine("Message 1.")
        iStyle.addLine("Message 2.")
        iStyle.addLine("Message 3.")
        iStyle.addLine("Message 4.")
        iStyle.addLine("Message 5.")
        iStyle.setSummaryText("+2 more")

        val notification = notificationBuilder
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIconBitmap)
            .setContentTitle("App is running in background")
            .setStyle(bigTextStyle)
            .addAction(R.drawable.ic_launcher_background, "Share", pendingIntent)

            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.")
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        Log.d(TAG_FOREGROUND_SERVICE, "onDestroy")
        super.onDestroy()
    }
}