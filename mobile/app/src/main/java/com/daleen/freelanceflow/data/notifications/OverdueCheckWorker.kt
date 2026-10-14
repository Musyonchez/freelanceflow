package com.daleen.freelanceflow.data.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daleen.freelanceflow.data.local.dao.ClientDao
import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OverdueCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val invoiceDao: InvoiceDao,
    private val clientDao: ClientDao,
    private val notifier: OverdueInvoiceNotifier,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()
        val newlyOverdue = invoiceDao.getNewlyOverdue(now)
        if (newlyOverdue.isEmpty()) return Result.success()

        invoiceDao.flagOverdue(now)

        for (invoice in newlyOverdue) {
            val clientName = clientDao.getById(invoice.clientId)?.name ?: "Client"
            notifier.notifyOverdue(invoice, clientName)
        }

        return Result.success()
    }
}
