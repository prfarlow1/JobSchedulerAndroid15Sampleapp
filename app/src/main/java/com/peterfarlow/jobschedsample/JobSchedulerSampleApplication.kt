package com.peterfarlow.jobschedsample

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class JobSchedulerSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    Log.d(LOG_TAG, "app started")
                }

                override fun onStop(owner: LifecycleOwner) {
                    Log.d(LOG_TAG, "app stopped")
                    owner.lifecycle.coroutineScope.launch {
                        delay(5_000)
                        Log.d(LOG_TAG, "scheduling")
                        schedule()
                    }
                }
            }
        )
    }

    private fun schedule() {
        val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(applicationContext, BackgroundService::class.java))
            .setOverrideDeadline(1.minutes.inWholeMilliseconds)
            .build()
        applicationContext.getSystemService<JobScheduler>()!!.schedule(jobInfo)
    }
}

private const val JOB_ID = 98435

const val LOG_TAG = "JobSchedulerSampleApplication"
