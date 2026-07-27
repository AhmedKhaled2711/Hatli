package com.app.hatli.core.data.user

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage,
) {
    suspend fun uploadUserImage(uid: String, localUri: String, relativePath: String): String {
        val uri = Uri.parse(localUri)
        val ref = storage.reference.child("users/$uid/$relativePath")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun uploadIdImage(uid: String, localUri: String): String =
        uploadUserImage(uid, localUri, "documents/id.jpg")

    suspend fun uploadSelfie(uid: String, localUri: String): String =
        uploadUserImage(uid, localUri, "selfie.jpg")
}
