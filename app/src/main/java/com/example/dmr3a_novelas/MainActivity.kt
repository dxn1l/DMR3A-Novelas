package com.example.dmr3a_novelas

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.room.Room
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.ui.AppNavegation.NovelApp
import com.example.dmr3a_novelas.ui.Loader.NovelLoader
import com.example.dmr3a_novelas.ui.Receiver.InternetConnectivityReceiver


class MainActivity : ComponentActivity(), LoaderManager.LoaderCallbacks<List<Novel>>{

    private lateinit var connectivityReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelApp()
        }
        registerConnectivityReceiver()
        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    private fun registerConnectivityReceiver() {
        connectivityReceiver = InternetConnectivityReceiver()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Novel>> {
        Log.d("MainActivity", "onCreateLoader called")
        return NovelLoader(this)
    }

    override fun onLoadFinished(loader: Loader<List<Novel>>, data: List<Novel>?) {
        Log.d("MainActivity", "onLoadFinished called")
    }

    override fun onLoaderReset(loader: Loader<List<Novel>>) {
        Log.d("MainActivity", "onLoaderReset called")
    }




}