package com.example.dmr3a_novelas.DataBase


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.mapNotNull

class FirebaseNovelRepository {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()


    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getAllNovels(onResult: (List<Novel>) -> Unit,
                     onError: (DatabaseError) -> Unit) {

        val userId = getUserId() ?: return

        database.child("users").child(userId).child("novels").addValueEventListener(object :
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
        val userId = getUserId() ?: return
        val newNovelRef = database.child("users").child(userId).child("novels").child(novel.id!!)
        newNovelRef.setValue(novel)
    }

    fun removeNovel(novel: Novel) {
        val userId = getUserId() ?: return
        getReviewsForNovel(novel, onResult = { reviews ->
            reviews.forEach { review ->
                deleteReview(review, onSuccess = {
                }, onError = { error ->
                })
            }
            novel.id?.let {
                database.child("users").child(userId).child("novels").child(it).removeValue()
            }
        }, onError = {})
    }

    fun getFavoriteNovels(onResult: (List<Novel>) -> Unit, onError: (DatabaseError) -> Unit) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("novels").addValueEventListener(object : ValueEventListener {
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
        val userId = getUserId() ?: return
        novel.id?.let {
            database.child("users").child(userId).child("novels").child(it).child("_isFavorite").setValue(!novel.getIsFavorite())
        }
    }

    fun addReview(id: String, novel: Novel, reviewText: String, usuario: String) {
        val userId = getUserId() ?: return
        val review = Review(id, novel.id!!, reviewText, usuario)
        database.child("users").child(userId).child("reviews").child(id).setValue(review)
    }

    fun getReviewsForNovel(novel: Novel, onResult: (List<Review>) -> Unit, onError: (DatabaseError) -> Unit) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("reviews").addValueEventListener(object : ValueEventListener {
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
        val userId = getUserId() ?: return
        val novelRef = database.child("users").child(userId).child("novels").child(novel.id!!)
        novelRef.setValue(novel)
    }

    fun getNovelById(id: String, onResult: (Novel) -> Unit, onError: (DatabaseError) -> Unit) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("novels").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
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
        val userId = getUserId() ?: return
        val reviewRef = database.child("users").child(userId).child("reviews").child(review.id!!)
        reviewRef.removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(DatabaseError.fromException(error)) }
    }

    fun checkIdExists(id: String, callback: (Boolean) -> Unit) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("reviews").child(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val review = task.result.value
                callback(review != null)
            } else {
                callback(false)
            }
        }
    }

}