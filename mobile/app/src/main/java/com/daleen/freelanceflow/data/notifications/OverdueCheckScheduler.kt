package com.daleen.freelanceflow.data.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverdueCheckScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /** Idempotent: safe to call on every app start, only schedules once. */
    fun schedulePeriodic() {
        val request = PeriodicWorkRequestBuilder<OverdueCheckWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    companion object {
        private const val WORK_NAME = "overdue_invoice_check"
    }
}
