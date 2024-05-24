package com.semenchenko.foodfriend.repository

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.model.Ingredient
import com.semenchenko.foodfriend.model.Recipe
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = "https://gslrvaoxsezybkztjuzh.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdzbHJ2YW94c2V6eWJrenRqdXpoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTIwMzg4MjIsImV4cCI6MjAyNzYxNDgyMn0.mhBuEEQJj0NevdmPHS7N37FKT8n4MzA3B36HA1k5Jdw"
) {
    install(Postgrest)
}

class SupabaseManager {

    suspend fun getDishes(): MutableList<Dish>? {
        try {
            val result: List<Dish> = supabase.from("dishes")
                .select().decodeList<Dish>()
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    suspend fun getIngredientById(id: Int): Ingredient? {
        try {
            val result: Ingredient = supabase
                .from("ingredients")
                .select {
                    filter { eq("id", id) }
                }.decodeSingle()
            println("result: $result")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getRecipe(id: Int): MutableList<Recipe>? {
        try {
            val result: List<Recipe> = supabase.from("recipes")
                .select {
                    filter { eq("dish_id", id) }
                }.decodeList<Recipe>()
            println(result.toString())
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun searchDishesByName(query: String): MutableList<Dish>? {
        try {
            val result = supabase
                .from("dishes")
                .select {
                    filter { ilike("name", "%$query%") }
                }
                .decodeList<Dish>()
            println("searchDishByName: $result")
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun searchIngredientsByName(query: String): MutableList<Ingredient>? {
        try {
            val result = supabase
                .from("ingredients")
                .select {
                    filter {
                        ilike("name", "%$query%")
                    }
                }
                .decodeList<Ingredient>()
            println("searchDishByName: $result")
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun addDish(name: String, description: String, image: String?): Dish? {
        val dish = DishInsert(name, description, image)
        try {
            val result: Dish = supabase.from("dishes").insert(dish) {
                select()
            }.decodeSingle<Dish>()
            println("new Dish: $result")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun addRecipe(list: MutableList<RecipeInsert>, view: View) {
        try {
            supabase.from("recipes").insert(list)
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Snackbar.make(view, "Error adding recipe: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    suspend fun getDishesByIds(list: List<Int>): MutableList<Dish>? {
        try {
            val result = supabase
                .from("dishes")
                .select {
                    filter { Dish::id isIn list }
                }
                .decodeList<Dish>()
            println("getDishesByIds: $result")
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getRecipesByDishIds(list: List<Int>): MutableList<Recipe>? {
        try {
            val result = supabase
                .from("recipes")
                .select {
                    filter { isIn("dish_id", list) }
                }
                .decodeList<Recipe>()
            println("getRecipesByIds: $result")
            return result.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

@Serializable
data class DishInsert(
    val name: String,
    val description: String,
    val image: String?,

)

@Serializable
data class RecipeInsert(
    @SerialName("dish_id")
    val dishId: Int,

    @SerialName("ingredient_id")
    val ingredientId: Int,
    val amount: Float
)