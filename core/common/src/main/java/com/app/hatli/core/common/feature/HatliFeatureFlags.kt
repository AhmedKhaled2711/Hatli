package com.app.hatli.core.common.feature

/**
 * Compile-time feature switches for environments that cannot use every Firebase product yet.
 *
 * Re-enable Storage uploads by flipping [STORAGE_IMAGE_UPLOADS_ENABLED] to `true` after
 * upgrading to the Blaze plan and deploying [storage.rules].
 */
object HatliFeatureFlags {
    /**
     * Firebase Storage requires Blaze. Spark plan: keep `false`.
     * When `true`: Runner ID image + selfie upload via [FirebaseStorageDataSource].
     */
    const val STORAGE_IMAGE_UPLOADS_ENABLED: Boolean = false
}
