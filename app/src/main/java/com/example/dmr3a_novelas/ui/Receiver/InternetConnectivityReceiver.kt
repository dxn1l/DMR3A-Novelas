package com.example.dmr3a_novelas.ui.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.dmr3a_novelas.ui.Notification.sendNotification
import com.example.dmr3a_novelas.ui.Syncronized.scheduleDataSyncJob



class InternetConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if(isInternetAvailables(context)){
            Log.d("InternetConnectivityReceiver", "Internet connected")
            sendNotification(
                context,
                "Internet Connected",
                "You are now connected to the internet",
                1
            )
            scheduleDataSyncJob(context)

        } else {
            Log.d("InternetConnectivityReceiver", "Internet disconnected")
            sendNotification(
                context,
                "Internet Disconnected",
                "You are now disconnected from the internet",
                1

            )
        }
    }
}

fun isInternetAvailables(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}