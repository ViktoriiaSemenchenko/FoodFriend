package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.DishSmallAdapter
import com.semenchenko.foodfriend.databinding.FragmentShoppingListBinding
import com.semenchenko.foodfriend.model.Dish

class ShoppingListFragment : Fragment(R.layout.fragment_shopping_list) {

    private lateinit var binding: FragmentShoppingListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShoppingListBinding.bind(view)

        val dishes = listOf(
            Dish(
                1,
                "Spaghetti Carbonara",
                "spaghetti_carbonara_image",
                "Classic Italian pasta dish made with eggs, cheese, pancetta, and black pepper.",
                "hgfd"
            ),
            Dish(
                2,
                "Chicken Alfredo",
                "chicken_alfredo_image",
                "Creamy pasta dish made with chicken, Parmesan cheese, and Alfredo sauce.",
                "hgfd"
            ),
            Dish(
                3,
                "Margherita Pizza",
                "margherita_pizza_image",
                "Traditional Italian pizza topped with tomato sauce, mozzarella cheese, and fresh basil.",
                "hgfd"
            ),
            Dish(
                4,
                "Caesar Salad",
                "caesar_salad_image",
                "Salad made with romaine lettuce, croutons, Parmesan cheese, and Caesar dressing.",
                "hgfd"
            ),
            Dish(
                5,
                "Beef Burger",
                "beef_burger_image",
                "Juicy beef patty served on a bun with lettuce, tomato, onion, and condiments.",
                "hgfd"
            )
        )

        binding.dishSmallRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            //addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        binding.dishSmallRecycler.adapter = DishSmallAdapter(dishes, context)

    }
}
