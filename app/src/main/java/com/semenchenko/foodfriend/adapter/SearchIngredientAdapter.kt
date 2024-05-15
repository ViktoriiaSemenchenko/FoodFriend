package com.semenchenko.foodfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.Ingredient
import com.semenchenko.foodfriend.viewmodel.AddUnitViewModel

class SearchIngredientAdapter(
    private val ingredients: MutableList<Ingredient>, private val addUnitViewModel: AddUnitViewModel,
    val activity: FragmentActivity?
) :
    RecyclerView.Adapter<SearchIngredientAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View, addUnitViewModel: AddUnitViewModel,
                                     activity: FragmentActivity?) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.search_name)

        init {
            itemView.setOnClickListener {
                addUnitViewModel.ingredientId.value = ingredients[layoutPosition].id
                addUnitViewModel.ingredientName.value = ingredients[layoutPosition].name
                addUnitViewModel.unit.value = ingredients[layoutPosition].unit
                //addNewRecipeViewModel.addIngredient(ingredients[layoutPosition])
                println("recipeInsertViewModel.ingredientId.value = ${addUnitViewModel.ingredientId.value.toString()}")
                println("recipeInsertViewModel.ingredientName.value = ${addUnitViewModel.ingredientName.value.toString()}")
                println("recipeInsertViewModel.unit.value = ${addUnitViewModel.unit.value.toString()}")

                (activity as MainActivity).navController.navigate(R.id.action_searchIngredient_to_addUnitFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_search, parent, false)
        return IngredientViewHolder(itemView, addUnitViewModel, activity)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredients[position]
        holder.name.text = currentItem.name
    }

    override fun getItemCount() = ingredients.size
}