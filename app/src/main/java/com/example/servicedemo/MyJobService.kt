package com.example.servicedemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast

private const val SERVICE_NAME = "MyJobService"
const val SAVED_INT_KEY = "int_key"

class MyJobService : JobService() {

    private val handler = Handler()
    lateinit var params: JobParameters
    lateinit var task: CounterTask

    override fun onCreate() {
        super.onCreate()
        showToast("MyJobService Created")
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        showToast("MyJobService onStopJob")
        task.cancel(true)
        Log.d(SERVICE_NAME,"onStopJob. Pause")
        return true
    }


    override fun onStartJob(params: JobParameters?): Boolean {
        showToast("MyJobService onStartJob")
        val data = params?.extras
        this.params = params!!
        val start = data?.getInt("maxCountValue")?:getValue()

        task = CounterTask(this,start)
        task.execute(Unit)

        return true
    }

    fun getValue(): Int {
        val prefs = getSharedPreferences("MyJobService", Context.MODE_PRIVATE)
        return prefs.getInt(SAVED_INT_KEY, 0)
    }

    fun notifyJobFinished() {
        Log.d(SERVICE_NAME,"Job finished. Calling jobFinished()")
        val prefs = getSharedPreferences("MyJobService", Context.MODE_PRIVATE)
        prefs.edit().putInt(SAVED_INT_KEY,0).apply()
        jobFinished(params,false)
    }

    override fun onDestroy() {
        super.onDestroy()
        showToast("MyJobService onDestroy")
    }

    fun showToast(msg: String) {
        handler.post {
            Toast.makeText(this@MyJobService, msg, Toast.LENGTH_SHORT).show()
        }
    }
}