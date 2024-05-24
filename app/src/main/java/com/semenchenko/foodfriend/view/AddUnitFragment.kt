package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.databinding.FragmentAddUnitBinding
import com.semenchenko.foodfriend.model.IngredientForList
import com.semenchenko.foodfriend.model.IngredientForRecipe
import com.semenchenko.foodfriend.viewmodel.AddNewRecipeViewModel
import com.semenchenko.foodfriend.viewmodel.AddUnitViewModel

class AddUnitFragment : Fragment(R.layout.fragment_add_unit) {
    private lateinit var binding: FragmentAddUnitBinding
    private val addUnitViewModel: AddUnitViewModel by activityViewModels()
    private val addNewRecipeViewModel: AddNewRecipeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUnitBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setBottomNavVisibility(false)

        binding.ingredientName.text = addUnitViewModel.ingredientName.value.toString()
        binding.ingredientUnit.text = addUnitViewModel.unit.value.toString()

        binding.ingredientCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addIngredient.setOnClickListener {
            val amount: String = binding.amount.editableText.toString()
            if (amount != "") {
                val ingredientForList = IngredientForList(
                    addUnitViewModel.ingredientId.value!!.toInt(),
                    addUnitViewModel.ingredientName.value.toString(),
                    amount.toFloat(),
                    addUnitViewModel.unit.value.toString()
                )
                println("recipeInsert: $ingredientForList")

                val ingredientForRecipe = IngredientForRecipe(
                    addUnitViewModel.ingredientId.value!!.toInt(),
                    amount.toFloat()
                )
                addNewRecipeViewModel.addIngredientForRecipe(ingredientForRecipe)
                addNewRecipeViewModel.addIngredientForAdapter(ingredientForList)
                println("addNewRecipeViewModel.ingredients.value: ${addNewRecipeViewModel.ingredientsListForAdapter.value.toString()}")
                println("addNewRecipeViewModel.ingredientsListForRecipe.value: ${addNewRecipeViewModel.ingredientsListForRecipe.value.toString()}")

                findNavController().navigate(R.id.action_addUnitFragment_to_addIngredientToNewRecipe)
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Enter the required amount of ingredient")
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()
            }
        }
    }
}