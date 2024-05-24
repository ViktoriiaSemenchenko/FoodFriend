package com.semenchenko.foodfriend.repository

import com.semenchenko.foodfriend.model.Ingredient
import com.semenchenko.foodfriend.model.IngredientAmount
import com.semenchenko.foodfriend.model.Recipe

class RecipeManager {
    private val shoppingMap = mutableMapOf<Ingredient, Float>()
    private val supabaseManager: SupabaseManager = SupabaseManager()

    private fun ingredientAmountMapToList(shoppingListMap: Map<Ingredient, Float>): List<IngredientAmount> {
        return shoppingListMap.map { (ingredient, quantity) ->
            IngredientAmount(ingredient, quantity)
        }
    }

    suspend fun getIngredientAmountList(recipe: MutableList<Recipe>): List<IngredientAmount>? {
        try {
            for (item in recipe) {
                val ingredient: Ingredient? = supabaseManager.getIngredientById(item.ingredientId)

                val currentAmount = shoppingMap.getOrDefault(ingredient, 0f)
                shoppingMap[ingredient!!] = currentAmount + item.amount
            }
            return ingredientAmountMapToList(shoppingMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}