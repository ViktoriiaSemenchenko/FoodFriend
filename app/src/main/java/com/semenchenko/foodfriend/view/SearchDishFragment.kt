package com.semenchenko.foodfriend.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.SearchDishAdapter
import com.semenchenko.foodfriend.databinding.FragmentSearchDishBinding
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

class SearchDishFragment : Fragment(R.layout.fragment_search_dish) {
    private lateinit var binding: FragmentSearchDishBinding
    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private val handler = Handler()
    private val delay: Long = 1000 // 1000 = 1 second
    private lateinit var searchQuery: String

    private val searchRunnable = Runnable {
        // Виконується після затримки
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchDishRecyclerView.adapter = supabaseManager.searchDishesByName(
                searchQuery
            )?.let { SearchDishAdapter(it, recipeViewModel, activity) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchDishBinding.bind(view)

        binding.searchView.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        (activity as MainActivity).setBottomNavVisibility(false)

        binding.searchCancel.setOnClickListener {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.searchDishRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        binding.searchView.doAfterTextChanged {
            handler.removeCallbacks(searchRunnable)
            if (it.toString().isNotEmpty()) {
                searchQuery = it?.toString()!!
                handler.postDelayed(searchRunnable, delay)
            }
        }
    }
}