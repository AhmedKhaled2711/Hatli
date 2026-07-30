package com.app.hatli.core.data.user

import android.content.Context
import android.net.Uri
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.feature.HatliFeatureFlags
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase Storage uploads for Runner verification images.
 *
 * Currently gated by [HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED] (Spark plan has no Storage).
 * Keep this class wired in DI so flipping the flag re-enables uploads with no architecture change.
 */
@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    @ApplicationContext private val context: Context,
) {
    suspend fun uploadUserImage(uid: String, localUri: String, relativePath: String): String {
        ensureStorageEnabled()
        val source = Uri.parse(localUri)
        val safeName = relativePath.replace('/', '_')
        val temp = File(context.cacheDir, "hatli_upload_${uid}_$safeName")
        try {
            copyUriToFile(source, temp)
            val ref = storage.reference.child("users/$uid/$relativePath")
            val contentType = context.contentResolver.getType(source) ?: "image/jpeg"
            val metadata = StorageMetadata.Builder()
                .setContentType(contentType)
                .build()
            val snapshot = ref.putFile(Uri.fromFile(temp), metadata).await()
            return snapshot.storage.downloadUrl.await().toString()
        } finally {
            temp.delete()
        }
    }

    suspend fun uploadIdImage(uid: String, localUri: String): String =
        uploadUserImage(uid, localUri, "documents/id.jpg")

    suspend fun uploadSelfie(uid: String, localUri: String): String =
        uploadUserImage(uid, localUri, "selfie.jpg")

    private fun ensureStorageEnabled() {
        if (!HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED) {
            throw AppError.FirebaseFailure(
                "Image upload is temporarily disabled (Firebase Storage requires Blaze).",
            )
        }
    }

    private fun copyUriToFile(source: Uri, dest: File) {
        val input = context.contentResolver.openInputStream(source)
            ?: throw AppError.InvalidInput(
                "Could not read the selected image. Please pick the photo again.",
            )
        input.use { stream ->
            dest.outputStream().use { output -> stream.copyTo(output) }
        }
        if (!dest.exists() || dest.length() == 0L) {
            throw AppError.InvalidInput(
                "Selected image is empty or unreadable. Please pick another photo.",
            )
        }
    }
}
