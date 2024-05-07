package com.semenchenko.foodfriend.repository

import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.model.Ingredient
import com.semenchenko.foodfriend.model.Recipe
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

val supabase = createSupabaseClient(
    supabaseUrl = "https://gslrvaoxsezybkztjuzh.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdzbHJ2YW94c2V6eWJrenRqdXpoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTIwMzg4MjIsImV4cCI6MjAyNzYxNDgyMn0.mhBuEEQJj0NevdmPHS7N37FKT8n4MzA3B36HA1k5Jdw"
) {
    install(Postgrest)
}

class SupabaseManager {

    suspend fun getDishes(): MutableList<Dish> {
        val dish: MutableList<Dish> = mutableListOf()
        try {
            val result: List<Dish> = supabase.from("dish")
                .select().decodeList<Dish>()
            dish.addAll(result)
            println(dish.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dish
    }


    suspend fun getIngredientNameById(id: Int): String {
        try {
            val result: Ingredient = supabase
                .from("ingredient")
                .select() {
                    filter { eq("id", id) }
                }.decodeSingle()
            println("name: ${result.name}")
            return result.name
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    suspend fun getRecipe(id: Int): MutableList<Recipe> {
        val recipe: MutableList<Recipe> = mutableListOf()
        try {
            val result: List<Recipe> = supabase.from("recipe")
                .select(){
                    filter { eq("dish_id", id) }
                }.decodeList<Recipe>()
            recipe.addAll(result)
            println(recipe.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return recipe
    }

    suspend fun searchDishesByName(query: String): MutableList<Dish> {
        val result = supabase
            .from("dish")
            .select(){
                filter {
                    ilike("name", "%$query%")
                }
            }
            .decodeList<Dish>()
        println("searchDishByName: $result")
        return result.toMutableList()
    }
}