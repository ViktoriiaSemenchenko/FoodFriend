package com.semenchenko.foodfriend.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.model.Dish

class DishSmallAdapter(private val dishes: List<Dish>, context: Context?) :
    RecyclerView.Adapter<DishSmallAdapter.DishSmallViewHolder>() {

    val testImageResource: Drawable? = ContextCompat.getDrawable(context!!, R.drawable.image_for_empty)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishSmallViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.holder_dish_card_small, parent, false)
        return DishSmallViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DishSmallViewHolder, position: Int) {
        val currentItem = dishes[position]
        holder.dishName.text = currentItem.name
        holder.imageView.setImageDrawable(testImageResource)
    }

    override fun getItemCount() = dishes.size

    class DishSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dishName: TextView = itemView.findViewById(R.id.dsv_dish_name)
        val imageView: ImageView = itemView.findViewById(R.id.dsv_image_view)

    }
}