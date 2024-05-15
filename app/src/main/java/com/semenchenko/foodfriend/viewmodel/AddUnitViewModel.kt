package com.semenchenko.foodfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUnitViewModel : ViewModel() {

    val ingredientId: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }
    val ingredientName: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val amount: MutableLiveData<Float> by lazy {
        MutableLiveData()
    }
    val unit: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
}