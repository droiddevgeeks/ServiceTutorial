package com.example.servicedemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyWorker(context: Context, workerParam: WorkerParameters) : Worker(context, workerParam) {

    companion object {
        const val TAG_MY_WORKED = "MyWorker"
        const val MESSAGE_STATUS = "message_status"
        const val WORKER_RESULT = "worker_result"
    }

    override fun doWork(): Result {
        Log.v(TAG_MY_WORKED, "doWork")
        val data = inputData
        showNotification(
            "WorkManager", data.getString(MESSAGE_STATUS) ?: "Message has been Sent"
        )

        val resultData = Data.Builder().putString(WORKER_RESULT, "Job Finished").build()
        return Result.success(resultData)
    }


    private fun showNotification(task: String, description: String) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channelId"
        val channelName = "channelName"

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(task)
            .setContentText(description)
            .setSmallIcon(R.mipmap.ic_launcher)

        manager.notify(1, builder.build())
    }
}