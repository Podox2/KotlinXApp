package com.podorozhniak.kotlinx.practice.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.podorozhniak.kotlinx.practice.util.NotificationUtil.showNotification

/*
* при використанні FirebaseMessagingService сповіщення може прийти в трьох випадках:
* 1. при запущеній аплікусі - в цьому випадку, сповіщення не буде показане (така політика Firebase),
* але його можна обробити в FirebaseMessagingService - отримати дані і створити сповіщення самостійно
* 2. при закритій або 3. згорнутій аплікусі - сповіщення буде показане автоматично
* */
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