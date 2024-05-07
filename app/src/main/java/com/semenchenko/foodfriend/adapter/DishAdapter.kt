package com.semenchenko.foodfriend.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel

class DishAdapter(
    private val dishes: MutableList<Dish>,
    private val recipeViewModel: RecipeViewModel,
    context: Context?,
    activity: FragmentActivity?
) :
    RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    val activity = activity
    val testImageResource: Drawable? =
        ContextCompat.getDrawable(context!!, R.drawable.image_for_empty)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_dish_card, parent, false)
        return DishViewHolder(itemView, activity, dishes, recipeViewModel)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val currentItem = dishes[position]
        holder.dishName.text = currentItem.name
        holder.imageView.setImageDrawable(testImageResource)
    }

    override fun getItemCount() = dishes.size

    class DishViewHolder(itemView: View, activity: FragmentActivity?, dishes: MutableList<Dish>, recipeViewModel: RecipeViewModel) :
        RecyclerView.ViewHolder(itemView) {

        val dishName: TextView = itemView.findViewById(R.id.dish_name)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)

        init {
            itemView.setOnClickListener {
                recipeViewModel.dish.value = dishes[layoutPosition]
                println("recipeViewModel.dish.value = ${recipeViewModel.dish.value}")
                (activity as MainActivity).navController.navigate(R.id.action_home_page_to_recipeFragment)
            }
        }
    }
}