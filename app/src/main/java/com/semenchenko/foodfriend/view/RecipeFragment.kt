package com.semenchenko.foodfriend.view

import android.graphics.BitmapFactory
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
import com.semenchenko.foodfriend.adapter.RecipeAdapter
import com.semenchenko.foodfriend.databinding.FragmentRecipeBinding
import com.semenchenko.foodfriend.model.Recipe
import com.semenchenko.foodfriend.repository.PhotoManager
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    private lateinit var binding: FragmentRecipeBinding
    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val photoManager = PhotoManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeBinding.bind(view)

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


        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val recipe: MutableList<Recipe> =
                supabaseManager.getRecipe(recipeViewModel.dish.value!!.id!!)
            println("recipe list $recipe")
            binding.ingredientsRecycler.adapter = RecipeAdapter(recipe)
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
    }
}