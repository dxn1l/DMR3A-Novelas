package com.example.dmr3a_novelas.DataBase


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.mapNotNull

class FirebaseNovelRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun getAllNovels(onResult: (List<Novel>) -> Unit) {
        database.child("novels").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novels = snapshot.children.mapNotNull { it.getValue(Novel::class.java) }
                onResult(novels)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }

    fun addNovel(novel: Novel) {
        val newNovelRef = database.child("novels").child(novel.id!!)
        newNovelRef.setValue(novel)
    }

    fun removeNovel(novel: Novel) {
        novel.id?.let {
            database.child("novels").child(it).removeValue()
        }
    }

    fun getFavoriteNovels(onResult: (List<Novel>) -> Unit) {
        database.child("novels").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteNovels = snapshot.children.mapNotNull { it.getValue(Novel::class.java) }
                    .filter { it.getIsFavorite() }
                onResult(favoriteNovels)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun toggleFavorite(novel: Novel) {
        novel.id?.let {
            database.child("novels").child(it).child("_isFavorite").setValue(!novel.getIsFavorite())
        }
    }

    fun addReview(novel: Novel, reviewText: String, usuario: String) {
        val newReviewRef = database.child("reviews").push()
        val review = Review(newReviewRef.key, novel.id!!, reviewText, usuario)
        newReviewRef.setValue(review)
    }

    fun getReviewsForNovel(novel: Novel, onResult: (List<Review>) -> Unit) {
        database.child("reviews").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                    .filter { it.novelId == novel.id }
                onResult(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}