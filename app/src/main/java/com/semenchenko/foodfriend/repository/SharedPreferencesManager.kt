package com.semenchenko.foodfriend.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val tag = "SharedPreferencesManager"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("dish_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun saveDishIds(dishIds: List<Int>) {
        val json: String = gson.toJson(dishIds)
        sharedPreferences.edit().putString("dish_ids", json).apply()
        Log.d(tag, "dish ids saved in sharedPreferences $json")
    }

    fun getDishIds(): List<Int> {
        val json = sharedPreferences.getString("dish_ids", null)
        return if (json != null) {
            val type = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addDishId(dishId: Int) {
        val dishIds = getDishIds().toMutableList()
        if (!dishIds.contains(dishId)) {
            dishIds.add(dishId)
            saveDishIds(dishIds)
        }
    }

    fun containsDishId(dishId: Int): Boolean {
        val dishIds = getDishIds()
        return dishIds.contains(dishId)
    }

    fun clearDishIds() {
        sharedPreferences.edit().remove("dish_ids").apply()
    }
}