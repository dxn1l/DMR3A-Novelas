package com.example.dmr3a_novelas.DataBase


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.mapNotNull

class FirebaseNovelRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun getAllNovels(onResult: (List<Novel>) -> Unit,
                     onError: (DatabaseError) -> Unit) {

        database.child("novels").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novels = snapshot.children.mapNotNull { it.getValue(Novel::class.java) }
                onResult(novels)
            }
            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    fun addNovel(novel: Novel) {
        val newNovelRef = database.child("novels").child(novel.id!!)
        newNovelRef.setValue(novel)
    }

    fun removeNovel(novel: Novel) {
        getReviewsForNovel(novel, onResult = { reviews ->
            reviews.forEach { review ->
                deleteReview(review, onSuccess = {
                }, onError = { error ->
                })
            }
            novel.id?.let {
                database.child("novels").child(it).removeValue()
            }
        }, onError = { error ->
        })
    }

    fun getFavoriteNovels(onResult: (List<Novel>) -> Unit, onError: (DatabaseError) -> Unit) {
        database.child("novels").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteNovels = snapshot.children.mapNotNull { it.getValue(Novel::class.java) }
                    .filter { it.getIsFavorite() }
                onResult(favoriteNovels)
            }
            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    fun toggleFavorite(novel: Novel) {
        novel.id?.let {
            database.child("novels").child(it).child("_isFavorite").setValue(!novel.getIsFavorite())
        }
    }

    fun addReview(id: String, novel: Novel, reviewText: String, usuario: String) {
        val review = Review(id, novel.id!!, reviewText, usuario)
        database.child("reviews").child(id).setValue(review)
    }

    fun getReviewsForNovel(novel: Novel, onResult: (List<Review>) -> Unit, onError: (DatabaseError) -> Unit) {
        database.child("reviews").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                    .filter { it.novelId == novel.id }
                onResult(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    fun updateNovel(novel: Novel) {
        val novelRef = database.child("novels").child(novel.id!!)
        novelRef.setValue(novel)
    }

    fun getNovelById(id: String, onResult: (Novel) -> Unit, onError: (DatabaseError) -> Unit) {
        database.child("novels").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novel = snapshot.getValue(Novel::class.java)
                if (novel != null) {
                    onResult(novel)
                } else {
                    onError(DatabaseError.fromException(Exception("No se encontró la novela con ID: $id")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }



    fun deleteReview(review: Review, onSuccess: () -> Unit, onError: (DatabaseError) -> Unit) {
        val reviewRef = database.child("reviews").child(review.id!!)
        reviewRef.removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(DatabaseError.fromException(error)) }
    }

    fun checkIdExists(id: String, callback: (Boolean) -> Unit) {
        database.child("reviews").child(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val review = task.result.value
                callback(review != null)
            } else {
                callback(false)
            }
        }
    }





}