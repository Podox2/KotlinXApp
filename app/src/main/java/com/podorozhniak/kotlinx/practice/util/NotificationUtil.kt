package com.podorozhniak.kotlinx.practice.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.view.MainActivity
import org.koin.java.KoinJavaComponent

object NotificationUtil {
    private const val CHANNEL_ID = "kotlin_x_channel_id"
    private const val CHANNEL_NAME = "KotlinX"
    private const val REQUEST_CODE = 113

    val context by KoinJavaComponent.inject(Context::class.java)
    private val notificationManager: NotificationManager

    init {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(remoteMessage: RemoteMessage) {
        val contentTitle = remoteMessage.notification?.title ?: "Notification title"
        val contentText = remoteMessage.notification?.body ?: "Notification text"
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(contentText.hashCode(), notificationBuilder.build())
    }

    fun checkNotificationAllowed(): Boolean = NotificationManagerCompat.from(context).areNotificationsEnabled()
}