package com.example.servicedemo

import android.content.Context
import android.os.AsyncTask
import android.util.Log

class CounterTask(private val params: MyJobService, var startInt: Int) :
    AsyncTask<Unit, Int, Unit>() {

    private val LIMIT = 100
    private var start = 0
    override fun onPreExecute() {
        super.onPreExecute()
        start = params.getValue()
    }

    override fun doInBackground(vararg params: Unit?) {
        for (i in start..LIMIT) {
            if (!isCancelled) {
                Thread.sleep(200)
                if (startInt < LIMIT) {
                    publishProgress(startInt++)
                }
            }
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("MyJobService", "Counter value: ${values[0]}")
        params.showToast("${values[0]}")
        val prefs = params.getSharedPreferences("MyJobService", Context.MODE_PRIVATE)
        values[0]?.let { prefs.edit().putInt(SAVED_INT_KEY, it).commit() }
    }

    override fun onPostExecute(result: Unit?) {
        params.notifyJobFinished()
    }
}