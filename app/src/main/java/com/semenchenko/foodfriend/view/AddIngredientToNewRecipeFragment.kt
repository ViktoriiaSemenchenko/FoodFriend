package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.AddRecipeIngredientsAdapter
import com.semenchenko.foodfriend.databinding.FragmentAddIngredientBinding
import com.semenchenko.foodfriend.model.IngredientForList
import com.semenchenko.foodfriend.viewmodel.AddNewRecipeViewModel
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddIngredientToNewRecipeFragment : Fragment(R.layout.fragment_add_ingredient) {
    private lateinit var binding: FragmentAddIngredientBinding
    private val addNewRecipeViewModel: AddNewRecipeViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddIngredientBinding.bind(view)

        (activity as MainActivity).setBottomNavVisibility(false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.addRecipeCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addIngredientToNewRecipe_to_homePage)
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.save.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                recipeViewModel.dish.value = addNewRecipeViewModel.addNewRecipe()
                findNavController().navigate(R.id.action_addIngredientToNewRecipe_to_recipeFragment)
            }
        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_addIngredientToNewRecipe_to_addNewRecipe)
        }

        binding.addIngredient.setOnClickListener {
            findNavController().navigate(R.id.action_addIngredientToNewRecipe_to_searchIngredient)
        }

        binding.ingredientsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        val ingredients: MutableList<IngredientForList>? =
            addNewRecipeViewModel.ingredientsListForAdapter.value
        println("ingredients: $ingredients")
        binding.ingredientsRecycler.adapter = ingredients?.let { AddRecipeIngredientsAdapter(it) }
    }
}