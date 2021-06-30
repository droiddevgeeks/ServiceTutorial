package com.example.servicedemo

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService

private const val SERVICE_NAME = "MyJobIntentService"

class MyJobIntentService : JobIntentService() {

    private val handler = Handler()

    companion object {
        private const val JOB_ID = 123

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, JOB_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        showToast("JobIntentService  Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showToast("JobIntentService  onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onHandleWork(intent: Intent) {
        val maxCount = intent.getIntExtra("maxCountValue", -1)

        for (i in 0 until maxCount) {
            Log.d(SERVICE_NAME, "onHandleWork: The number is: $i")
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                Log.d(SERVICE_NAME, "Exception: ")
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showToast("JobIntentService onDestroy")
    }

    private fun showToast(msg: String) {
        handler.post {
            Toast.makeText(this@MyJobIntentService, msg, Toast.LENGTH_LONG).show()
        }
    }
}