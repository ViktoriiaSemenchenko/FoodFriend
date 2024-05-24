package com.semenchenko.foodfriend.viewmodel

import androidx.lifecycle.ViewModel
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.repository.SupabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomePageViewModel : ViewModel() {

    private val supabaseManager: SupabaseManager = SupabaseManager()

    suspend fun getDishesFromDB(): MutableList<Dish>? {
        return withContext(Dispatchers.IO) {
            supabaseManager.getDishes()
        }
    }
}