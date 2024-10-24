package com.example.dmr3a_novelas.ui.Loader

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel

class NovelLoader(context: Context) : AsyncTaskLoader<List<Novel>>(context) {

    private val novelRepository = FirebaseNovelRepository()

    override fun loadInBackground(): List<Novel>? {
        Log.d("NovelLoader", "loadInBackground called")
        var novels: List<Novel>? = null
        novelRepository.getAllNovels(
            onResult = { result ->
                novels = result
                Log.d("NovelLoader", "Novels loaded: ${novels?.size}")
            },
            onError = { error ->
                Log.e("NovelLoader", "Error loading novels: ${error.message}")
            }
        )
        return novels
    }

    override fun onStartLoading() {
        Log.d("NovelLoader", "onStartLoading called")
        forceLoad()
    }
}