package com.daleen.freelanceflow.data.receipts

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.File
import javax.inject.Inject

private const val BUCKET = "receipts"

/**
 * Local-first storage for expense receipt photos. The on-device file at
 * [localFile] is the source of truth for display -- it exists as soon as a
 * photo is captured/imported, independently of whether it has been uploaded
 * yet. [Expense.receiptPhotoUrl] only ever holds the Supabase Storage object
 * key, set once [upload] has actually run during a sync.
 */
class ReceiptStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient,
) {
    private val receiptsDir = File(context.filesDir, "receipts").apply { mkdirs() }

    fun localFile(expenseId: String): File = File(receiptsDir, "$expenseId.jpg")

    fun newCameraUri(expenseId: String): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", localFile(expenseId))

    fun importFromGallery(expenseId: String, sourceUri: Uri): File {
        val dest = localFile(expenseId)
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        }
        return dest
    }

    fun deleteLocalCopy(expenseId: String) {
        localFile(expenseId).delete()
    }

    suspend fun upload(userId: String, expenseId: String): String {
        val storagePath = "$userId/$expenseId.jpg"
        val bytes = localFile(expenseId).readBytes()
        supabaseClient.storage.from(BUCKET).upload(storagePath, bytes) { upsert = true }
        return storagePath
    }

    suspend fun deleteRemote(storagePath: String) {
        supabaseClient.storage.from(BUCKET).delete(storagePath)
    }

    suspend fun ensureLocalCopy(storagePath: String, expenseId: String): File? {
        val file = localFile(expenseId)
        if (file.exists()) return file
        return try {
            val bytes = supabaseClient.storage.from(BUCKET).downloadAuthenticated(storagePath)
            file.writeBytes(bytes)
            file
        } catch (e: Exception) {
            null
        }
    }
}
