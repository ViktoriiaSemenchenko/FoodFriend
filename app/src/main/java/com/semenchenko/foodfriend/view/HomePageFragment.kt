package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.DishAdapter
import com.semenchenko.foodfriend.databinding.FragmentHomePageBinding
import com.semenchenko.foodfriend.viewmodel.HomePageViewModel
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var binding: FragmentHomePageBinding
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val homePageViewModel: HomePageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomePageBinding.bind(view)

        binding.dishRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val dishes = homePageViewModel.getDishesFromDB()
                    if (!dishes.isNullOrEmpty()) {
                        binding.dishRecyclerView.adapter =
                            DishAdapter(dishes, recipeViewModel, context, activity)
                        binding.swipeRefreshLayout.isRefreshing = false
                    } else {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Something happened...")
                            .setMessage("Server error, check your internet connection and restart application")
                            .setPositiveButton("OK") { _, _ ->
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                            .show()
                    }
                } finally {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            homePageViewModel.getDishesFromDB()?.let {
                binding.dishRecyclerView.adapter =
                    DishAdapter(it, recipeViewModel, context, activity)
                binding.progressBar.visibility = View.GONE
            } ?: run {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.something_happened))
                    .setMessage(getString(R.string.server_error_check_your_internet_connection))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    .show()
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.VISIBLE
            }
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_search)
        }
    }
}