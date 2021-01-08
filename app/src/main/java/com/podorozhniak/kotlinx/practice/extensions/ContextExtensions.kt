package com.podorozhniak.kotlinx.practice.extensions

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.openAppNotificationSettings() {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    startActivity(intent)
}