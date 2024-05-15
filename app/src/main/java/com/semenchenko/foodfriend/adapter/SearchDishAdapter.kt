package com.semenchenko.foodfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel

class SearchDishAdapter(
    private val dishes: MutableList<Dish>, private val recipeViewModel: RecipeViewModel,
    val activity: FragmentActivity?
) :
    RecyclerView.Adapter<SearchDishAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View, recipeViewModel: RecipeViewModel,
                               activity: FragmentActivity?) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.search_name)

        init {
            itemView.setOnClickListener {
                recipeViewModel.dish.value = dishes[layoutPosition]
                println("recipeViewModel.dish.value = ${recipeViewModel.dish.value}")
                (activity as MainActivity).navController.navigate(R.id.action_search_to_recipeFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_search, parent, false)
        return DishViewHolder(itemView, recipeViewModel, activity)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val currentItem = dishes[position]
        holder.name.text = currentItem.name
    }

    override fun getItemCount() = dishes.size
}