package com.example.dmr3a_novelas.ui.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dmr3a_novelas.ui.Receiver.ReminderReceiver

fun scheduleReminder(context: Context) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


    val interval = 15 * 60 *1000L

    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis(),
        interval,
        pendingIntent
    )

}