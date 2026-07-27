package com.app.hatli.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * FCM entry point. Feature-specific handling belongs in dedicated use cases later.
 */
class HatliFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // Persist / upload via a repository in a later phase.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Route to notification presenter / feature handlers later.
    }
}
