package com.example.servicedemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.servicedemo.MyWorker.Companion.TAG_MY_WORKED
import com.example.servicedemo.MyWorker.Companion.WORKER_RESULT


class MainActivity : AppCompatActivity() {

    private val workManager: WorkManager by lazy { WorkManager.getInstance() }
    private val workRequest: OneTimeWorkRequest by lazy {
        OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(getWorkConstraint())
            .setInputData(getData()).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeWorkManagerStatus()
    }

    fun onStartIntentService(view: View?) {
        val mIntent = Intent(this@MainActivity, MyIntentService::class.java)
        mIntent.putExtra("maxCountValue", 100)
        startService(mIntent)
    }

    fun onStartJobIntentService(view: View?) {
        val mIntent = Intent(this@MainActivity, MyJobIntentService::class.java)
        mIntent.putExtra("maxCountValue", 100)
        MyJobIntentService.enqueueWork(this, mIntent)
    }

    fun onStartJobScheduler(view: View?) {
        scheduleMyJob()
    }

    private fun scheduleMyJob() {
        val JOB_ID = 321
        val jobScheduler =
            applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val componentName = ComponentName(this@MainActivity, MyJobService::class.java)
        val bundle = PersistableBundle()
        bundle.putInt("maxCountValue", 100)
        val jobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setExtras(bundle)
            .build()
        jobScheduler.schedule(jobInfo)
    }

    fun onStartForeGroundService(view: View?) {
        val intent = Intent(this@MainActivity, MyForeGroundService::class.java)
        intent.action = MyForeGroundService.ACTION_START_FOREGROUND_SERVICE
        ContextCompat.startForegroundService(this, intent)
    }

    fun onStopForeGroundService(view: View?) {
        val intent = Intent(this@MainActivity, MyForeGroundService::class.java)
        intent.action = MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE
        ContextCompat.startForegroundService(this, intent)
    }


    fun onWorkManagerClick(view: View?) {
        workManager.enqueueUniqueWork(
            TAG_MY_WORKED,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun getData() = Data.Builder()
        .putString(MyWorker.MESSAGE_STATUS, "The task data passed from MainActivity")
        .build()

    private fun getWorkConstraint() = Constraints.Builder()
        .build()


    private fun observeWorkManagerStatus() {
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer<WorkInfo> { workInfo ->
                workInfo?.let {
                    val state = it.state
                    Log.v(MyWorker.TAG_MY_WORKED, state.toString())
                    val result = it.outputData.getString(WORKER_RESULT)
                    if (state.isFinished)
                        Toast.makeText(this@MainActivity, "Data$result", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity, state.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }
}
