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
import com.semenchenko.foodfriend.repository.PhotoManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel

class DishSmallAdapter(
    private val dishes: List<Dish>, context: Context?,
    private val recipeViewModel: RecipeViewModel,
    val activity: FragmentActivity?
) :
    RecyclerView.Adapter<DishSmallAdapter.DishSmallViewHolder>() {

    private val testImageResource: Drawable? =
        ContextCompat.getDrawable(context!!, R.drawable.image_for_empty)
    private val photoManager = PhotoManager()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishSmallViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_dish_card_small, parent, false)
        return DishSmallViewHolder(itemView, recipeViewModel, dishes, activity)
    }

    override fun onBindViewHolder(holder: DishSmallViewHolder, position: Int) {
        val currentItem = dishes[position]
        holder.dishName.text = currentItem.name
        if (currentItem.image.isNullOrEmpty()) {
            holder.imageView.setImageDrawable(testImageResource)
        } else {
            holder.imageView.setImageBitmap(photoManager.base64ToBitmap(currentItem.image))
        }
    }

    override fun getItemCount() = dishes.size

    class DishSmallViewHolder(
        itemView: View,
        recipeViewModel: RecipeViewModel,
        dishes: List<Dish>,
        activity: FragmentActivity?
    ) : RecyclerView.ViewHolder(itemView) {

        val dishName: TextView = itemView.findViewById(R.id.dsv_dish_name)
        val imageView: ImageView = itemView.findViewById(R.id.dsv_image_view)

        init {
            itemView.setOnClickListener {
                recipeViewModel.dish.value = dishes[layoutPosition]
                println("recipeViewModel.dish.value = ${recipeViewModel.dish.value}")
                (activity as MainActivity).navController.navigate(R.id.action_shoppingList_to_recipeFragment)
            }
        }
    }
}