package com.semenchenko.foodfriend.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.IngredientAmountAdapter
import com.semenchenko.foodfriend.databinding.FragmentRecipeBinding
import com.semenchenko.foodfriend.model.Recipe
import com.semenchenko.foodfriend.repository.PhotoManager
import com.semenchenko.foodfriend.repository.RecipeManager
import com.semenchenko.foodfriend.repository.SharedPreferencesManager
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    private lateinit var binding: FragmentRecipeBinding

    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val photoManager: PhotoManager = PhotoManager()
    private val recipeManager = RecipeManager()

    private val tag = "RecipeFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeBinding.bind(view)

        val sharedPreferencesManager = SharedPreferencesManager(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_recipeFragment_to_homePage)
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        (activity as MainActivity).setBottomNavVisibility(false)

        binding.roundButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipeFragment_to_homePage)
            (activity as MainActivity).setBottomNavVisibility(true)
        }
        binding.ingredientsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        val dishId: Int = recipeViewModel.dish.value!!.id!!
        when {
            sharedPreferencesManager.containsDishId(dishId) -> {
                binding.addItemsToShoppingList.text = getString(R.string.items_added)
                binding.addItemsToShoppingList.isEnabled = false
                Log.d(tag, "dish id contains in sharedPreferences $dishId")
            }

            else -> binding.addItemsToShoppingList.text =
                getString(R.string.add_items_to_shopping_list)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val recipe: MutableList<Recipe>? =
                supabaseManager.getRecipe(dishId)
            println("recipe list $recipe")
            if (!recipe.isNullOrEmpty()) {
                binding.ingredientsRecycler.adapter = recipeManager.getIngredientAmountList(
                    recipe
                )?.let { IngredientAmountAdapter(it) }
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Something happened...")
                    .setMessage("Server error, check your internet connection")
                    .setPositiveButton("OK") { _, _ ->
                        findNavController().navigate(R.id.action_recipeFragment_to_homePage)
                        (activity as MainActivity).setBottomNavVisibility(true)
                    }
                    .show()
            }
            binding.progressBar.visibility = View.GONE
        }

        binding.dishName.text = recipeViewModel.dish.value!!.name
        binding.description.text = recipeViewModel.dish.value!!.description
        binding.imageView.setImageBitmap(recipeViewModel.dish.value!!.image?.let {
            photoManager.base64ToBitmap(
                it
            )
        } ?: run {
            BitmapFactory.decodeResource(resources, R.drawable.image_for_empty)
        })


        binding.addItemsToShoppingList.setOnClickListener {
            sharedPreferencesManager.addDishId(dishId)
            binding.addItemsToShoppingList.text = getString(R.string.items_added)
        }
    }
}