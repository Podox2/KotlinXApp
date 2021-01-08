package com.podorozhniak.kotlinx.practice.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.podorozhniak.kotlinx.practice.util.NotificationUtil.showNotification

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val NOTIFICATION_TAG = "NOTIFICATION_TAG"
    }

    override fun onNewToken(newFirebaseToken: String) {
        super.onNewToken(newFirebaseToken)
        Log.d(NOTIFICATION_TAG, "onNewToken - $newFirebaseToken")
        //save token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(NOTIFICATION_TAG, "onMessageReceived")
        showNotification(remoteMessage)
    }
}