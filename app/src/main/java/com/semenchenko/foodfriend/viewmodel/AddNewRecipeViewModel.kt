package com.semenchenko.foodfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.model.IngredientForList
import com.semenchenko.foodfriend.model.IngredientForRecipe
import com.semenchenko.foodfriend.repository.RecipeInsert
import com.semenchenko.foodfriend.repository.SupabaseManager

class AddNewRecipeViewModel: ViewModel() {
    private val supabaseManager = SupabaseManager()

    val dishName: MutableLiveData<String?> by lazy {
        MutableLiveData()
    }

    val dishDescription: MutableLiveData<String?> by lazy {
        MutableLiveData()
    }

    val image: MutableLiveData<String?> by lazy {
        MutableLiveData()
    }

    val ingredientsListForAdapter: MutableLiveData<MutableList<IngredientForList>?> by lazy {
        MutableLiveData()
    }

    val ingredientsListForRecipe: MutableLiveData<MutableList<IngredientForRecipe>?> by lazy {
        MutableLiveData()
    }

    val uniqueId: MutableLiveData<String?> by lazy {
        MutableLiveData()
    }

    fun clearData(){
        dishName.value = null
        dishDescription.value = null
        image.value = null
        ingredientsListForRecipe.value = null
        ingredientsListForAdapter.value = null
        uniqueId.value = null
    }
    fun addIngredientForAdapter(ingredient: IngredientForList) {
        val currentList = ingredientsListForAdapter.value ?: mutableListOf()
        currentList.add(ingredient)
        ingredientsListForAdapter.value = currentList
    }

    fun addIngredientForRecipe(ingredient: IngredientForRecipe){
        val currentList = ingredientsListForRecipe.value ?: mutableListOf()
        currentList.add(ingredient)
        ingredientsListForRecipe.value = currentList
    }

    suspend fun addNewRecipe(): Dish{
        val newDish = supabaseManager.addDish(
            name = dishName.value!!,
            description = dishDescription.value!!,
            image = image.value,
            uniqueId.value!!
        )
        println("new Dish image: ${newDish?.image}")
        println("new Dish id: ${newDish?.id}")

        val recipeList = mutableListOf<RecipeInsert>()
        newDish!!.id.let {
            for(i in ingredientsListForRecipe.value!!){
                recipeList.add(RecipeInsert(it!!, i.ingredientId, i.amount))
                println("recipeList: ${recipeList.toString()}")
            }
        }
        supabaseManager.addRecipe(recipeList)
        return newDish
    }
}