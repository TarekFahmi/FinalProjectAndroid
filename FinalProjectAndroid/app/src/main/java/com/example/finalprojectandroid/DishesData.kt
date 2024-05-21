package com.example.finalprojectandroid

data class DishesData(
    val id:String?  = "",
    val dishName:String? = "",
    val price :Double? = 0.00,
    val photoUrl : String? = "",
    val categoryName : String? = "",
    var quantity : Int = 1
)
