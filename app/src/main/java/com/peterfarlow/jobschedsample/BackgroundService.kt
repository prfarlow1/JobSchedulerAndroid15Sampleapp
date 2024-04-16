package com.peterfarlow.jobschedsample

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BackgroundService : JobService() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onStartJob(params: JobParameters): Boolean {
        coroutineScope.launch {
            fakeWork(params)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(LOG_TAG, "job stopped")
        coroutineScope.cancel()
        return false
    }

    private suspend fun fakeWork(params: JobParameters) {
        repeat(5) {
            Log.d(LOG_TAG, "starting work $it")
            delay(1_000)
        }
        jobFinished(params, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
