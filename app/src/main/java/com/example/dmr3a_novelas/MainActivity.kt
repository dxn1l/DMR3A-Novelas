package com.example.dmr3a_novelas

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.dmr3a_novelas.ui.AppNavegation.NovelApp
import com.example.dmr3a_novelas.ui.Syncronized.DataSyncJobService


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelApp()
        }
        scheduleJob()
    }


    private fun scheduleJob() {
        val componentName = ComponentName(this, DataSyncJobService::class.java)
        val jobInfo = JobInfo.Builder(1, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(2 * 60 * 1000)
            .build()

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }
}






