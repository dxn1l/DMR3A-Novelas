package com.example.dmr3a_novelas.ui.Notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.dmr3a_novelas.ui.BroadCast.InternetConnectivityReceiver.Companion.CHANNEL_ID

fun DeleteNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(CHANNEL_ID)
    }
}