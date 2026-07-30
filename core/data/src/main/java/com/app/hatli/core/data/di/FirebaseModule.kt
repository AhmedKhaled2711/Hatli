package com.app.hatli.core.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Why inject Firebase services?
 * - Single source of truth for SDK instances
 * - Easy to swap fakes in instrumented/unit tests
 * - Avoids hidden global state (FirebaseAuth.getInstance() everywhere)
 *
 * Trade-off: slightly more wiring; huge win for testability and scoping.
 *
 * Note: [FirebaseStorage] stays provided even while [HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED]
 * is false (Spark plan), so uploads can be re-enabled without DI changes.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}
