package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.DishSmallAdapter
import com.semenchenko.foodfriend.adapter.IngredientAmountAdapter
import com.semenchenko.foodfriend.databinding.FragmentShoppingListBinding
import com.semenchenko.foodfriend.model.Dish
import com.semenchenko.foodfriend.model.Recipe
import com.semenchenko.foodfriend.repository.RecipeManager
import com.semenchenko.foodfriend.repository.SharedPreferencesManager
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

class ShoppingListFragment : Fragment(R.layout.fragment_shopping_list) {

    private lateinit var binding: FragmentShoppingListBinding
    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val recipeManager = RecipeManager()
    private val recipeViewModel: RecipeViewModel by activityViewModels()


    private val tag = "ShoppingListFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShoppingListBinding.bind(view)

        val sharedPreferencesManager = SharedPreferencesManager(requireContext())

        binding.dishSmallRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        binding.ingredientsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        if (sharedPreferencesManager.getDishIds().isEmpty()) {
            binding.recipeHeader.visibility = View.INVISIBLE
            binding.ingredients.visibility = View.INVISIBLE
            binding.shoppingListEmpty.visibility = View.VISIBLE
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.progressIndicator1.visibility = View.VISIBLE
                    binding.progressIndicator2.visibility = View.VISIBLE

                    val dishIds = sharedPreferencesManager.getDishIds()
                    val dishes: MutableList<Dish>? = supabaseManager.getDishesByIds(dishIds)

                    if (!dishes.isNullOrEmpty()) {
                        Log.d(tag, "getDishesByIds $dishes")

                        val recipes: MutableList<Recipe>? = supabaseManager.getRecipesByDishIds(dishIds)
                        if (!recipes.isNullOrEmpty()) {
                            if (isAdded) {
                                binding.ingredientsRecycler.adapter =
                                    recipeManager.getIngredientAmountList(recipes)
                                        ?.let { IngredientAmountAdapter(it) }
                                binding.progressIndicator2.visibility = View.GONE
                                binding.dishSmallRecycler.adapter =
                                    DishSmallAdapter(dishes, context, recipeViewModel, activity)
                                binding.progressIndicator1.visibility = View.GONE
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(tag, "Error loading data", e)
                } finally {
                    if (isAdded) {
                        binding.progressIndicator1.visibility = View.GONE
                        binding.progressIndicator2.visibility = View.GONE
                    }
                }
            }
        }

        binding.delete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Want to clear the shopping cart?")
                .setPositiveButton("Yes") { _, _ ->
                    sharedPreferencesManager.clearDishIds()
                    findNavController().popBackStack()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
    }
}
