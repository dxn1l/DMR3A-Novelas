package com.example.dmr3a_novelas

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.dmr3a_novelas.ui.AppNavegation.NovelApp
import com.example.dmr3a_novelas.ui.BroadCast.InternetConnectivityReceiver

class MainActivity : ComponentActivity() {
    private lateinit var connectivityReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelApp()
        }
        registerConnectivityReceiver()
    }

    private fun registerConnectivityReceiver() {
        connectivityReceiver = InternetConnectivityReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }


}