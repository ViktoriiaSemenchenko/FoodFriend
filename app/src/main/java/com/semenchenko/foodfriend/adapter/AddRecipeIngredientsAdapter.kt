package com.semenchenko.foodfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.IngredientForList

class AddRecipeIngredientsAdapter(
    private val ingredients: MutableList<IngredientForList>
) : RecyclerView.Adapter<AddRecipeIngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_ingredient, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.amount.text = ingredient.amount.toString()
        holder.unit.text = ingredient.unit
        holder.ingredientName.text = ingredient.ingredientName
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredient_name)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val unit: TextView = itemView.findViewById(R.id.unit)
    }
}