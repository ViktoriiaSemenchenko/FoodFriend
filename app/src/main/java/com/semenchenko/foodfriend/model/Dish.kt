package com.semenchenko.foodfriend.model

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Int?,
    val name: String,
    val image: String?,
    val description: String,
)
