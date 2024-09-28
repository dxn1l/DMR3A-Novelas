package com.example.dmr3a_feedback1.DataBase

data class Novel(
    val title: String,
    val author: String,
    val year: Int,
    val synopsis: String,
    var _isFavorite: Boolean = false

) {

    fun getIsFavorite(): Boolean = _isFavorite
    fun setFavorite(favorite: Boolean) { _isFavorite = favorite }


}