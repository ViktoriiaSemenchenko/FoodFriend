package com.semenchenko.foodfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.semenchenko.foodfriend.model.Dish

class RecipeViewModel : ViewModel() {

    val dish: MutableLiveData<Dish> by lazy {
        MutableLiveData()
    }

}