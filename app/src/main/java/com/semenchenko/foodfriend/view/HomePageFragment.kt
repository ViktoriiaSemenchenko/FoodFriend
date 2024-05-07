package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.DishAdapter
import com.semenchenko.foodfriend.databinding.FragmentHomePageBinding
import com.semenchenko.foodfriend.viewmodel.HomePageViewModel
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val dishes = homePageViewModel.getDishesFromDB()
                    binding.dishRecyclerView.adapter = DishAdapter(dishes, recipeViewModel, context, activity)
                } finally {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            binding.dishRecyclerView.adapter =
                DishAdapter(homePageViewModel.getDishesFromDB(), recipeViewModel, context, activity)
            binding.progressBar.visibility = View.GONE
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_search)
        }
    }
}