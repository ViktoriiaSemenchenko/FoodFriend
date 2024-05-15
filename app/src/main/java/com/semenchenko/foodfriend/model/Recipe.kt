package com.semenchenko.foodfriend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int?,

    @SerialName("dish_id")
    val dishId: Int,

    @SerialName("ingredient_id")
    val ingredientId: Int,
    val amount: Float
)
