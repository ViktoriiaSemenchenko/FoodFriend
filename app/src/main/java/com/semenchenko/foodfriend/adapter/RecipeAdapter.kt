package com.semenchenko.foodfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.Recipe
import com.semenchenko.foodfriend.repository.SupabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeAdapter(
    private val recipes: MutableList<Recipe>
) : RecyclerView.Adapter<RecipeAdapter.IngredientViewHolder>() {

    private val supabaseManager: SupabaseManager = SupabaseManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_ingredient, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.amount.text = recipe.amount.toString()
        GlobalScope.launch(Dispatchers.Main) {
            holder.ingredientName.text = supabaseManager.getIngredientById(recipe.ingredientId)?.name
            holder.unit.text = supabaseManager.getIngredientById(recipe.ingredientId)?.unit
        }
    }

    override fun getItemCount(): Int = recipes.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredient_name)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val unit: TextView = itemView.findViewById(R.id.unit)
    }
}