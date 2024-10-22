package com.example.dmr3a_novelas.ui.Syncronized


import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository

class DataSyncJobService : JobService() {
    private val novelRepository = FirebaseNovelRepository()

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("DataSyncJobService", "Job started")


        novelRepository.getAllNovels(
            onResult = { novels ->
                Log.d("DataSyncJobService", "Data synchronized")
                    jobFinished(params, false)
            },
            onError = { error ->
                Log.e("DataSyncJobService", "Error syncing data: ${error.message}")
                jobFinished(params, true)
            }
        )
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("DataSyncJobService", "Job stopped")
        return true
    }

}