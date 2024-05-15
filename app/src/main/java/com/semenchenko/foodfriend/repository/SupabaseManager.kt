package com.semenchenko.foodfriend.repository

import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.model.Ingredient
import com.semenchenko.foodfriend.model.Recipe
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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


    suspend fun getIngredientById(id: Int): Ingredient? {
        try {
            val result: Ingredient = supabase
                .from("ingredient")
                .select() {
                    filter { eq("id", id) }
                }.decodeSingle()
            println("result: ${result.toString()}")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getRecipe(id: Int): MutableList<Recipe> {
        val recipe: MutableList<Recipe> = mutableListOf()
        try {
            val result: List<Recipe> = supabase.from("recipe")
                .select() {
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
            .select() {
                filter {
                    ilike("name", "%$query%")
                }
            }
            .decodeList<Dish>()
        println("searchDishByName: $result")
        return result.toMutableList()
    }

    suspend fun searchIngredientsByName(query: String): MutableList<Ingredient> {
        val result = supabase
            .from("ingredient")
            .select() {
                filter {
                    ilike("name", "%$query%")
                }
            }
            .decodeList<Ingredient>()
        println("searchDishByName: $result")
        return result.toMutableList()
    }

    suspend fun addDish(name: String, description: String, image: String, uuid: String): Dish? {
        val dish = DishInsert(name, description, image, uuid)
        try {
            val result: Dish = supabase.from("dish").insert(dish) {
                select()
            }.decodeSingle<Dish>()
            println("new Dish: ${result.toString()}")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun addRecipe(list: MutableList<RecipeInsert>) {
        try {
            supabase.from("recipe").insert(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Serializable
data class DishInsert(
    val name: String,
    val description: String,
    val image: String,

    @SerialName("unique_id")
    val uniqueId: String
)

@Serializable
data class RecipeInsert(
    @SerialName("dish_id")
    val dishId: Int,

    @SerialName("ingredient_id")
    val ingredientId: Int,
    val amount: Float
)