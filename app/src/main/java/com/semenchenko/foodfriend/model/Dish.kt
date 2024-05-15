package com.semenchenko.foodfriend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Int?,
    val name: String,
    val image: String?,
    val description: String,

    @SerialName("unique_id")
    val uniqueId: String
)
