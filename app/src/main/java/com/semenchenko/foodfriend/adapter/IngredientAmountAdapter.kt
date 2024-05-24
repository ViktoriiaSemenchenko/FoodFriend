package com.semenchenko.foodfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.IngredientAmount

class IngredientAmountAdapter(
    private val shoppingList: List<IngredientAmount>
) : RecyclerView.Adapter<IngredientAmountAdapter.ShoppingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.holder_ingredient, parent, false)
        return ShoppingListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingListItem = shoppingList[position]
        holder.ingredientName.text = shoppingListItem.ingredient.name
        holder.amount.text = shoppingListItem.amount.toString()
        holder.unit.text = shoppingListItem.ingredient.unit
    }

    override fun getItemCount(): Int = shoppingList.size

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredient_name)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val unit: TextView = itemView.findViewById(R.id.unit)
    }
}