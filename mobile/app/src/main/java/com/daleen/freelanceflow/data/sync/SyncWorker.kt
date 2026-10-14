package com.daleen.freelanceflow.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daleen.freelanceflow.domain.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncRepository: SyncRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = syncRepository.syncAll().fold(
        onSuccess = { Result.success() },
        onFailure = { Result.retry() },
    )
}
