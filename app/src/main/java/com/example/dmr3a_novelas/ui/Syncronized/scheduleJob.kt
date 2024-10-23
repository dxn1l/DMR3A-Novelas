package com.example.dmr3a_novelas.ui.Syncronized

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.example.dmr3a_novelas.ui.Receiver.isInternetAvailables

fun scheduleDataSyncJob(context: Context) {

    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    val jobId = 1

    val existingJob = jobScheduler.allPendingJobs.find { it.id == jobId }
    if (existingJob != null) {
        Log.d("DataSyncJobService", "Data sync job already scheduled")
        return
    }

    val componentName = ComponentName(context, DataSyncJobService::class.java)
    val jobInfo = JobInfo.Builder(jobId, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPersisted(true)
        .setPeriodic(15 * 60 * 1000)
        .build()

    jobScheduler.schedule(jobInfo)
    Log.d("DataSyncJobService", "Data sync job scheduled")
}