package com.example.dmr3a_novelas.ui.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.dmr3a_novelas.ui.Notification.sendNotification

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent){
    Log.d("ReminderReceiver", "Alarm triggered, sending notification")

        sendNotification(
            context,
            "Recordatorio",
            "Recuerda leer tu novela favorita",
            4

        )

    }
}