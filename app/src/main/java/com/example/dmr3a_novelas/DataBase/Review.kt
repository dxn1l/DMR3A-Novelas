package com.example.dmr3a_novelas.DataBase

data class Review(
    val id : String? = null,
    val novelId: String = "",
    val reviewText: String = "",
    val usuario: String = ""
    ){

    constructor() : this(null, "", "", "")

}
