package com.example.servicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "CompleteReceiver", Toast.LENGTH_LONG).show()
        if (intent!!.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            fun startIntentService() {
                val mIntent = Intent(context, MyIntentService::class.java).apply {
                    val value = intent.getIntExtra("maxCountValue", 0)
                    Log.v("MyIntentService", "$value")
                    this.putExtra("maxCountValue", value)
                }
                context?.startService(mIntent)
            }

            fun startJobIntentService() {
                val mIntent = Intent(context, MyJobIntentService::class.java).apply {
                    val value = intent.getIntExtra("maxCountValue", 0)
                    Log.v("MyJobIntentService", "$value")
                    this.putExtra("maxCountValue", value)
                }
                MyJobIntentService.enqueueWork(context!!, mIntent)
            }
            startJobIntentService()
            startIntentService()
        }
    }
}