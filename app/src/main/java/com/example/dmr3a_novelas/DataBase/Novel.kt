package com.example.dmr3a_novelas.DataBase

data class Novel(
    var id: String? = null,
    val title: String = "",
    val author: String = "",
    val year: Int = 0,
    val synopsis: String = "",
    var _isFavorite: Boolean = false
) {

    fun getIsFavorite(): Boolean = _isFavorite
    fun setFavorite(favorite: Boolean) { _isFavorite = favorite }

    constructor() : this(null, "", "", 0, "", false)

}