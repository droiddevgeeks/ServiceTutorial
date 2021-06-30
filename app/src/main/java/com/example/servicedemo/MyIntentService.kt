package com.example.servicedemo

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast


private const val SERVICE_NAME = "MyIntentService"

class MyIntentService : IntentService(SERVICE_NAME) {
    private val handler = Handler()

    override fun onCreate() {
        super.onCreate()
        showToast("IntentService Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showToast("IntentService onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        showToast("IntentService onDestroy")
    }
    override fun onHandleIntent(intent: Intent?) {
        val maxCount = intent!!.getIntExtra("maxCountValue", -1)
        for (i in 0 until maxCount) {
            Log.d(SERVICE_NAME, "onHandleWork: The number is: $i")
            try {
                Thread.sleep(100)
            } catch (e: Throwable) {
                Log.d(SERVICE_NAME, "Exception: ")
                e.printStackTrace()
            }
        }
    }

    private fun showToast(msg: String) {
        handler.post {
            Toast.makeText(this@MyIntentService, msg, Toast.LENGTH_LONG).show()
        }
    }
}