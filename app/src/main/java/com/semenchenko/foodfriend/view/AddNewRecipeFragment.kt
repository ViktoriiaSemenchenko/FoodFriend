package com.semenchenko.foodfriend.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.databinding.FragmentAddNewRecipeBinding
import com.semenchenko.foodfriend.viewmodel.AddNewRecipeViewModel
import java.util.UUID

class AddNewRecipeFragment : Fragment(R.layout.fragment_add_new_recipe) {

    private lateinit var binding: FragmentAddNewRecipeBinding
    private val addNewRecipeViewModel: AddNewRecipeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewRecipeBinding.bind(view)

        (activity as MainActivity).setBottomNavVisibility(false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.addRecipeCancel.setOnClickListener {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                binding.next.visibility = View.GONE
            } else {
                binding.next.visibility = View.VISIBLE
            }
        }

        binding.next.setOnClickListener {
            addNewRecipeViewModel.dishName.value = binding.dishName.text.toString()
            addNewRecipeViewModel.dishDescription.value = binding.dishDescription.text.toString()
            addNewRecipeViewModel.uniqueId.value = UUID.randomUUID().toString()

            findNavController().navigate(R.id.action_addNewRecipe_to_addIngredientToNewRecipe)
        }

    }

    override fun onPause() {
        super.onPause()
        println("onPause")
        println("addNewRecipeViewModel.dishName.value: ${addNewRecipeViewModel.dishName.value.toString()}")
        println("addNewRecipeViewModel.dishDescription.value: ${addNewRecipeViewModel.dishDescription.value.toString()}")
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }
}