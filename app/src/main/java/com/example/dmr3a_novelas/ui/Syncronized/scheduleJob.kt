package com.example.dmr3a_novelas.ui.Syncronized

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

fun scheduleJob(context: Context) {
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    jobScheduler.cancel(1)

    val componentName = ComponentName(context, DataSyncJobService::class.java)
    val jobInfo = JobInfo.Builder(1, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPersisted(true)
        .setPeriodic(15 * 60 * 1000)
        .build()

    jobScheduler.schedule(jobInfo)
}

 fun cancelJob(context: Context) {
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.cancel(1)
}