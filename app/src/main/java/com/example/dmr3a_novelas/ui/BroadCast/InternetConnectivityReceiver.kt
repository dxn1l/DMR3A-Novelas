package com.example.dmr3a_novelas.ui.BroadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.dmr3a_novelas.ui.Notification.DeleteNotificationChannel
import com.example.dmr3a_novelas.ui.Notification.createNotificationChannel
import com.example.dmr3a_novelas.ui.Notification.sendNotification
import com.example.dmr3a_novelas.ui.Syncronized.cancelJob
import com.example.dmr3a_novelas.ui.Syncronized.scheduleJob

class InternetConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val network = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

                if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    Log.d("InternetConnectivityReceiver", "Internet connected")
                    createNotificationChannel(context)
                    sendNotification(
                        context,
                        "Internet Connected",
                        "You are now connected to the internet",
                        1
                    )
                    scheduleJob(context)
                } else {
                    Log.d("InternetConnectivityReceiver", "Internet disconnected")
                    DeleteNotificationChannel(context)
                    cancelJob(context)
                }
            }


    companion object {
        const val CHANNEL_ID = "novel_notifications_channel"
    }
}